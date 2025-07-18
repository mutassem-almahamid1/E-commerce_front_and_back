import { Navbar } from "@/components/layout/Navbar";
import { Footer } from "@/components/layout/Footer";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { useEffect, useState } from "react";
import { useAuth } from "@/context/AuthContext";
import { useNavigate } from "react-router-dom";
import { apiClient } from "@/lib/api";
import { Order } from "@/types";
import { Loader2, Package, Truck, CheckCircle, XCircle } from "lucide-react";

const Orders = () => {
  const { user } = useAuth();
  const [orders, setOrders] = useState<Order[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!user) return;

    const fetchOrders = async () => {
      try {
        setLoading(true);
        const data = await apiClient.getOrdersByUserId(user.id);
        setOrders(data);
      } catch (err: any) {
        setError(err.message || 'فشل في تحميل الطلبات');
        setOrders([]);
      } finally {
        setLoading(false);
      }
    };

    fetchOrders();
  }, [user]);

  const getStatusIcon = (status: Order['status']) => {
    switch (status) {
      case 'PENDING':
      case 'CONFIRMED':
        return <Package className="h-4 w-4" />;
      case 'PROCESSING':
      case 'SHIPPED':
        return <Truck className="h-4 w-4" />;
      case 'DELIVERED':
        return <CheckCircle className="h-4 w-4" />;
      case 'CANCELLED':
      case 'RETURNED':
        return <XCircle className="h-4 w-4" />;
      default:
        return <Package className="h-4 w-4" />;
    }
  };

  const getStatusColor = (status: Order['status']) => {
    switch (status) {
      case 'PENDING':
        return 'bg-yellow-100 text-yellow-800 border-yellow-200';
      case 'CONFIRMED':
        return 'bg-blue-100 text-blue-800 border-blue-200';
      case 'PROCESSING':
        return 'bg-purple-100 text-purple-800 border-purple-200';
      case 'SHIPPED':
        return 'bg-orange-100 text-orange-800 border-orange-200';
      case 'DELIVERED':
        return 'bg-green-100 text-green-800 border-green-200';
      case 'CANCELLED':
        return 'bg-red-100 text-red-800 border-red-200';
      case 'RETURNED':
        return 'bg-gray-100 text-gray-800 border-gray-200';
      default:
        return 'bg-gray-100 text-gray-800 border-gray-200';
    }
  };

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString('ar-SA', {
      year: 'numeric',
      month: 'long',
      day: 'numeric'
    });
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <main className="container mx-auto px-4 py-8">
          <div className="flex items-center justify-center h-64">
            <Loader2 className="h-8 w-8 animate-spin" />
            <span className="ml-2">جاري تحميل الطلبات...</span>
          </div>
        </main>
        <Footer />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <main className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-foreground mb-4">طلباتي</h1>
          <p className="text-muted-foreground">عرض تاريخ الطلبات وتفاصيلها</p>
        </div>

        {error && (
          <Card className="mb-6 border-red-200 bg-red-50">
            <CardContent className="p-4">
              <p className="text-red-600">{error}</p>
            </CardContent>
          </Card>
        )}

        {orders.length === 0 ? (
          <Card>
            <CardContent className="text-center py-12">
              <Package className="h-16 w-16 mx-auto text-muted-foreground mb-4" />
              <h3 className="text-lg font-semibold mb-2">لا توجد طلبات</h3>
              <p className="text-muted-foreground mb-4">لم تقم بأي طلبات بعد</p>
              <Button onClick={() => navigate('/products')}>
                تصفح المنتجات
              </Button>
            </CardContent>
          </Card>
        ) : (
          <div className="space-y-6">
            {orders.map((order) => (
              <Card key={order.id} className="hover:shadow-lg transition-shadow">
                <CardHeader>
                  <div className="flex justify-between items-start">
                    <div>
                      <CardTitle className="text-lg">
                        طلب رقم: {order.orderNumber}
                      </CardTitle>
                      <p className="text-sm text-muted-foreground">
                        تاريخ الطلب: {formatDate(order.createdAt)}
                      </p>
                    </div>
                    <Badge className={`${getStatusColor(order.status)} flex items-center gap-1`}>
                      {getStatusIcon(order.status)}
                      {order.statusDescription}
                    </Badge>
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-4">
                    <div>
                      <p className="text-sm font-medium text-muted-foreground">إجمالي المنتجات</p>
                      <p className="text-lg font-semibold">{order.totalItems} منتج</p>
                    </div>
                    <div>
                      <p className="text-sm font-medium text-muted-foreground">المبلغ الإجمالي</p>
                      <p className="text-lg font-semibold">${order.totalAmount.toFixed(2)}</p>
                    </div>
                    <div>
                      <p className="text-sm font-medium text-muted-foreground">المبلغ النهائي</p>
                      <p className="text-lg font-semibold text-green-600">${order.finalAmount.toFixed(2)}</p>
                    </div>
                  </div>

                  {order.discountAmount > 0 && (
                    <div className="mb-4 p-3 bg-green-50 border border-green-200 rounded-lg">
                      <p className="text-sm text-green-700">
                        تم توفير ${order.discountAmount.toFixed(2)} من خلال الخصم
                      </p>
                    </div>
                  )}

                  {order.trackingNumber && (
                    <div className="mb-4 p-3 bg-blue-50 border border-blue-200 rounded-lg">
                      <p className="text-sm text-blue-700">
                        رقم التتبع: <span className="font-mono">{order.trackingNumber}</span>
                      </p>
                    </div>
                  )}

                  <div className="flex justify-between items-center">
                    <div className="flex items-center gap-2">
                      <span className="text-sm text-muted-foreground">طريقة الدفع:</span>
                      <Badge variant="outline">{order.paymentMethod}</Badge>
                      {order.isPaid && (
                        <Badge className="bg-green-100 text-green-800">مدفوع</Badge>
                      )}
                    </div>
                    <Button
                      size="sm"
                      variant="outline"
                      onClick={() => navigate(`/orders/${order.id}`)}
                    >
                      عرض التفاصيل
                    </Button>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>
        )}
      </main>
      <Footer />
    </div>
  );
};

export default Orders;
