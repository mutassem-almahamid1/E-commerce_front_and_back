import { Navbar } from "@/components/layout/Navbar";
import { Footer } from "@/components/layout/Footer";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Separator } from "@/components/ui/separator";
import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { apiClient } from "@/lib/api";
import { Order } from "@/types";
import {
  Loader2,
  Package,
  Truck,
  CheckCircle,
  XCircle,
  MapPin,
  CreditCard,
  Clock,
  ArrowLeft
} from "lucide-react";

const OrderDetails = () => {
  const { orderId } = useParams<{ orderId: string }>();
  const [order, setOrder] = useState<Order | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();

  useEffect(() => {
    if (!orderId) {
      navigate('/orders');
      return;
    }

    const fetchOrder = async () => {
      try {
        setLoading(true);
        const data = await apiClient.getOrderById(orderId);
        setOrder(data);
      } catch (err: any) {
        setError(err.message || 'فشل في تحميل تفاصيل الطلب');
      } finally {
        setLoading(false);
      }
    };

    fetchOrder();
  }, [orderId, navigate]);

  const getStatusIcon = (status: Order['status']) => {
    switch (status) {
      case 'PENDING':
      case 'CONFIRMED':
        return <Package className="h-5 w-5" />;
      case 'PROCESSING':
      case 'SHIPPED':
        return <Truck className="h-5 w-5" />;
      case 'DELIVERED':
        return <CheckCircle className="h-5 w-5" />;
      case 'CANCELLED':
      case 'RETURNED':
        return <XCircle className="h-5 w-5" />;
      default:
        return <Package className="h-5 w-5" />;
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
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    });
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <main className="container mx-auto px-4 py-8">
          <div className="flex items-center justify-center h-64">
            <Loader2 className="h-8 w-8 animate-spin" />
            <span className="ml-2">جاري تحميل تفاصيل الطلب...</span>
          </div>
        </main>
        <Footer />
      </div>
    );
  }

  if (error || !order) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <main className="container mx-auto px-4 py-8">
          <Card className="border-red-200 bg-red-50">
            <CardContent className="text-center py-12">
              <XCircle className="h-16 w-16 mx-auto text-red-500 mb-4" />
              <h3 className="text-lg font-semibold mb-2">خطأ في تحميل الطلب</h3>
              <p className="text-muted-foreground mb-4">{error}</p>
              <Button onClick={() => navigate('/orders')}>
                العودة للطلبات
              </Button>
            </CardContent>
          </Card>
        </main>
        <Footer />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <main className="container mx-auto px-4 py-8">
        {/* Header */}
        <div className="mb-8">
          <Button
            variant="ghost"
            onClick={() => navigate('/orders')}
            className="mb-4"
          >
            <ArrowLeft className="h-4 w-4 mr-2" />
            العودة للطلبات
          </Button>
          <div className="flex justify-between items-start">
            <div>
              <h1 className="text-4xl font-bold text-foreground mb-2">
                طلب رقم: {order.orderNumber}
              </h1>
              <p className="text-muted-foreground">
                تاريخ الطلب: {formatDate(order.createdAt)}
              </p>
            </div>
            <Badge className={`${getStatusColor(order.status)} flex items-center gap-2 px-4 py-2`}>
              {getStatusIcon(order.status)}
              {order.statusDescription}
            </Badge>
          </div>
        </div>

        <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
          {/* Order Items */}
          <div className="lg:col-span-2">
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Package className="h-5 w-5" />
                  المنتجات ({order.totalItems})
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-4">
                  {order.items.map((item, index) => (
                    <div key={index} className="flex gap-4 p-4 border rounded-lg">
                      {item.imageUrl && (
                        <img
                          src={item.imageUrl}
                          alt={item.productName}
                          className="w-16 h-16 object-cover rounded"
                        />
                      )}
                      <div className="flex-1">
                        <h4 className="font-semibold">{item.productName}</h4>
                        {item.brandName && (
                          <p className="text-sm text-muted-foreground">
                            العلامة التجارية: {item.brandName}
                          </p>
                        )}
                        <div className="flex justify-between items-center mt-2">
                          <span className="text-sm">الكمية: {item.quantity}</span>
                          <div className="text-right">
                            <p className="text-sm text-muted-foreground">
                              ${item.price.toFixed(2)} × {item.quantity}
                            </p>
                            <p className="font-semibold">
                              ${item.totalPrice.toFixed(2)}
                            </p>
                          </div>
                        </div>
                      </div>
                    </div>
                  ))}
                </div>
              </CardContent>
            </Card>
          </div>

          {/* Order Summary & Details */}
          <div className="space-y-6">
            {/* Order Summary */}
            <Card>
              <CardHeader>
                <CardTitle>ملخص الطلب</CardTitle>
              </CardHeader>
              <CardContent className="space-y-3">
                <div className="flex justify-between">
                  <span>المجموع الفرعي:</span>
                  <span>${order.totalAmount.toFixed(2)}</span>
                </div>
                {order.discountAmount > 0 && (
                  <div className="flex justify-between text-green-600">
                    <span>الخصم:</span>
                    <span>-${order.discountAmount.toFixed(2)}</span>
                  </div>
                )}
                <div className="flex justify-between">
                  <span>الشحن:</span>
                  <span>
                    {order.shippingCost === 0 ? 'مجاني' : `$${order.shippingCost.toFixed(2)}`}
                  </span>
                </div>
                <div className="flex justify-between">
                  <span>الضريبة:</span>
                  <span>${order.taxAmount.toFixed(2)}</span>
                </div>
                <Separator />
                <div className="flex justify-between font-bold text-lg">
                  <span>المجموع النهائي:</span>
                  <span>${order.finalAmount.toFixed(2)}</span>
                </div>
              </CardContent>
            </Card>

            {/* Payment Info */}
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <CreditCard className="h-5 w-5" />
                  معلومات الدفع
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-2">
                  <div className="flex justify-between">
                    <span>طريقة الدفع:</span>
                    <Badge variant="outline">{order.paymentMethod}</Badge>
                  </div>
                  <div className="flex justify-between">
                    <span>حالة الدفع:</span>
                    <Badge className={order.isPaid ? 'bg-green-100 text-green-800' : 'bg-yellow-100 text-yellow-800'}>
                      {order.isPaid ? 'مدفوع' : 'غير مدفوع'}
                    </Badge>
                  </div>
                  {order.paymentId && (
                    <div className="flex justify-between text-sm">
                      <span>معرف الدفع:</span>
                      <span className="font-mono">{order.paymentId}</span>
                    </div>
                  )}
                </div>
              </CardContent>
            </Card>

            {/* Shipping Address */}
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <MapPin className="h-5 w-5" />
                  عنوان الشحن
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="text-sm space-y-1">
                  <p>{order.shippingAddress.street}</p>
                  <p>{order.shippingAddress.city}, {order.shippingAddress.state}</p>
                  <p>{order.shippingAddress.zipCode}</p>
                  <p>{order.shippingAddress.country}</p>
                </div>
              </CardContent>
            </Card>

            {/* Timeline */}
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Clock className="h-5 w-5" />
                  تتبع الطلب
                </CardTitle>
              </CardHeader>
              <CardContent>
                <div className="space-y-3">
                  <div className="flex items-center gap-3">
                    <div className="w-3 h-3 bg-blue-500 rounded-full"></div>
                    <div>
                      <p className="font-medium">تم إنشاء الطلب</p>
                      <p className="text-sm text-muted-foreground">
                        {formatDate(order.createdAt)}
                      </p>
                    </div>
                  </div>
                  {order.updatedAt && (
                    <div className="flex items-center gap-3">
                      <div className="w-3 h-3 bg-purple-500 rounded-full"></div>
                      <div>
                        <p className="font-medium">تم تحديث الطلب</p>
                        <p className="text-sm text-muted-foreground">
                          {formatDate(order.updatedAt)}
                        </p>
                      </div>
                    </div>
                  )}
                  {order.shippedAt && (
                    <div className="flex items-center gap-3">
                      <div className="w-3 h-3 bg-orange-500 rounded-full"></div>
                      <div>
                        <p className="font-medium">تم الشحن</p>
                        <p className="text-sm text-muted-foreground">
                          {formatDate(order.shippedAt)}
                        </p>
                      </div>
                    </div>
                  )}
                  {order.deliveredAt && (
                    <div className="flex items-center gap-3">
                      <div className="w-3 h-3 bg-green-500 rounded-full"></div>
                      <div>
                        <p className="font-medium">تم التسليم</p>
                        <p className="text-sm text-muted-foreground">
                          {formatDate(order.deliveredAt)}
                        </p>
                      </div>
                    </div>
                  )}
                </div>
                {order.trackingNumber && (
                  <div className="mt-4 p-3 bg-blue-50 border border-blue-200 rounded-lg">
                    <p className="text-sm text-blue-700">
                      رقم التتبع: <span className="font-mono font-bold">{order.trackingNumber}</span>
                    </p>
                  </div>
                )}
              </CardContent>
            </Card>

            {order.notes && (
              <Card>
                <CardHeader>
                  <CardTitle>ملاحظات</CardTitle>
                </CardHeader>
                <CardContent>
                  <p className="text-sm">{order.notes}</p>
                </CardContent>
              </Card>
            )}
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default OrderDetails;
