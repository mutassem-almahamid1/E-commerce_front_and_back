import { Navbar } from "@/components/layout/Navbar";
import { Footer } from "@/components/layout/Footer";
import { useCart } from "@/hooks/useCart";
import { Button } from "@/components/ui/button";
import { useAuth } from "@/context/AuthContext";
import { useNavigate } from "react-router-dom";
import { apiClient } from "@/lib/api";
import { useState, useEffect } from "react";
import { toast } from "@/hooks/use-toast";

const Cart = () => {
  const { cart, removeFromCart, addToCart, loadCartFromServer, clearCart } =
    useCart();
  const { user } = useAuth();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  // Loading cart from server on component mount
  useEffect(() => {
    if (user) {
      loadCartFromServer();
    }
  }, [user, loadCartFromServer]);

  const handleCheckout = async () => {
    if (!user) {
      navigate("/login");
      return;
    }

    if (cart.items.length === 0) {
      toast({
        title: "Empty Cart",
        description: "Please add items to cart before checkout",
        variant: "destructive",
      });
      return;
    }

    setLoading(true);
    try {
      // Build order data as expected by the backend
      const orderData = {
        items: cart.items.map((item) => ({
          productId: item.product.id,
          quantity: item.quantity,
        })),
        shippingAddress: {
          street: "123 Main St", // يجب جمع العنوان الفعلي من المستخدم
          city: "New York",
          state: "NY",
          zipCode: "10001",
          country: "United States",
          isDefault: true,
        },
        billingAddress: null, // سيستخدم نفس عنوان الشحن
        paymentMethod: "CASH_ON_DELIVERY", // تأكد من استخدام enum value الصحيح
        notes: "Order placed from cart",
        couponCode: null,
      };

      console.log("Sending order data:", orderData); // للتأكد من البيانات المرسلة

      const order = await apiClient.createOrder(user.id, orderData);

      toast({
        title: "Order Created Successfully!",
        description: `Order #${order.orderNumber} has been created`,
      });

      // تفريغ السلة بعد إنشاء الطلب بنجاح
      await clearCart();

      // Navigate to order details or orders page after creation
      navigate(`/orders/${order.id}`);
    } catch (error: any) {
      console.error("Order creation error:", error);

      let errorMessage = "Failed to create order. Please try again.";
      if (error.response?.data?.message) {
        errorMessage = error.response.data.message;
      } else if (error.message) {
        errorMessage = error.message;
      }

      toast({
        title: "Order Failed",
        description: errorMessage,
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <main className="container mx-auto px-4 py-8">
        <h1 className="text-4xl font-bold text-foreground mb-4">Cart</h1>
        <p className="text-muted-foreground mb-8">
          View and manage your shopping cart items.
        </p>
        {cart.items.length === 0 ? (
          <div className="text-center text-muted-foreground">
            Your cart is empty.
          </div>
        ) : (
          <div className="overflow-x-auto">
            <table className="min-w-full bg-card rounded-lg">
              <thead>
                <tr>
                  <th className="p-2 text-left">Image</th>
                  <th className="p-2 text-left">Product</th>
                  <th className="p-2 text-left">Quantity</th>
                  <th className="p-2 text-left">Price</th>
                  <th className="p-2 text-left">Total</th>
                  <th className="p-2 text-left">Action</th>
                </tr>
              </thead>
              <tbody>
                {cart.items.map((item) => (
                  <tr key={item.product.id} className="border-b">
                    <td className="p-2">
                      <img
                        src={
                          item.product.images && item.product.images.length > 0
                            ? item.product.images[0]
                            : item.product.image
                        }
                        alt={item.product.name}
                        className="w-12 h-12 object-cover rounded"
                      />
                    </td>
                    <td className="p-2">{item.product.name}</td>
                    <td className="p-2 flex items-center gap-2">
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => addToCart(item.product, -1)}
                        disabled={item.quantity <= 1}
                      >
                        -
                      </Button>
                      <span>{item.quantity}</span>
                      <Button
                        size="sm"
                        variant="outline"
                        onClick={() => addToCart(item.product, 1)}
                      >
                        +
                      </Button>
                    </td>
                    <td className="p-2">${item.product.price.toFixed(2)}</td>
                    <td className="p-2">
                      ${(
                        item.product.price * item.quantity
                      ).toFixed(2)}
                    </td>
                    <td className="p-2">
                      <Button
                        size="sm"
                        variant="destructive"
                        onClick={() => removeFromCart(item.product.id)}
                      >
                        Remove
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            <div className="flex justify-end mt-6">
              <div className="bg-card p-4 rounded-lg shadow">
                <div className="font-semibold text-lg mb-2">Cart Summary</div>
                <div>Total Items: {cart.itemCount}</div>
                <div>
                  Total Price:{" "}
                  <span className="font-bold">${cart.total.toFixed(2)}</span>
                </div>
                <Button
                  className="mt-4 w-full"
                  variant="premium"
                  onClick={handleCheckout}
                  disabled={loading}
                >
                  {loading ? "Processing..." : "Checkout"}
                </Button>
              </div>
            </div>
          </div>
        )}
      </main>
      <Footer />
    </div>
  );
};

export default Cart;
