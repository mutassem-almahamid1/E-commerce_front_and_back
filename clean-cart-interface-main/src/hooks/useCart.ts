import { useSelector, useDispatch } from 'react-redux';
import { RootState, AppDispatch } from '@/store';
import {
  addToCart as addToCartAction,
  removeFromCart as removeFromCartAction,
  updateQuantity,
  clearCart as clearCartAction,
  fetchCart,
  addToCartAPI,
  removeFromCartAPI,
  clearCartAPI
} from '@/store/slices/cartSlice';
import { Product, CartItem } from '@/types';
import { useAuth } from '@/context/AuthContext';

export const useCart = () => {
  const dispatch = useDispatch<AppDispatch>();
  const cart = useSelector((state: RootState) => state.cart);
  const { user } = useAuth();

  // إضافة منتج إلى السلة
  const addToCart = async (product: Product, quantity: number = 1) => {
    // إضافة محلية فورية للاستجابة السريعة
    dispatch(addToCartAction({ product, quantity }));

    // إذا كان المستخدم مسجل دخول، أرسل إلى الباك إند
    if (user) {
      try {
        const cartItem: CartItem = {
          productId: product.id,
          quantity,
          price: product.price
        };
        await dispatch(addToCartAPI({ userId: user.id, cartItem }));
      } catch (error) {
        console.error('Failed to add item to cart on server:', error);
        // يمكن إضافة معالجة خطأ هنا
      }
    }
  };

  // إزالة منتج من السلة
  const removeFromCart = async (productId: string) => {
    // إزالة محلية فورية
    dispatch(removeFromCartAction(productId));

    // إذا كان المستخدم مسجل دخول، أرسل إلى الباك إند
    if (user) {
      try {
        await dispatch(removeFromCartAPI({ userId: user.id, productId }));
      } catch (error) {
        console.error('Failed to remove item from cart on server:', error);
        // يمكن إضافة معالجة خطأ هنا
      }
    }
  };

  // تحديث كمية منتج
  const updateProductQuantity = async (productId: string, quantity: number) => {
    if (quantity <= 0) {
      await removeFromCart(productId);
      return;
    }

    dispatch(updateQuantity({ productId, quantity }));

    // إذا كان المستخدم مسجل دخول، نحتاج إلى تحديث الباك إند
    if (user) {
      try {
        const item = cart.items.find(item => item.product.id === productId);
        if (item) {
          const cartItem: CartItem = {
            productId,
            quantity,
            price: item.product.price
          };
          await dispatch(addToCartAPI({ userId: user.id, cartItem }));
        }
      } catch (error) {
        console.error('Failed to update item quantity on server:', error);
      }
    }
  };

  // تفريغ السلة
  const clearCart = async () => {
    dispatch(clearCartAction());

    if (user) {
      try {
        await dispatch(clearCartAPI(user.id));
      } catch (error) {
        console.error('Failed to clear cart on server:', error);
      }
    }
  };

  // تحميل السلة من الباك إند عند تسجيل الدخول
  const loadCartFromServer = async () => {
    if (user) {
      try {
        await dispatch(fetchCart(user.id));
      } catch (error) {
        console.error('Failed to load cart from server:', error);
      }
    }
  };

  // التحقق من وجود منتج في السلة
  const isInCart = (productId: string) => {
    return cart.items.some(item => {
      // Handle both possible structures: item.product.id or item.productId
      const itemProductId = item.product?.id || (item as any).productId;
      return itemProductId === productId;
    });
  };

  // الحصول على كمية منتج معين في السلة
  const getItemQuantity = (productId: string) => {
    const item = cart.items.find(item => {
      // Handle both possible structures: item.product.id or item.productId
      const itemProductId = item.product?.id || (item as any).productId;
      return itemProductId === productId;
    });
    return item ? item.quantity : 0;
  };

  return {
    cart,
    addToCart,
    removeFromCart,
    updateQuantity: updateProductQuantity,
    clearCart,
    loadCartFromServer,
    isInCart,
    getItemQuantity,
  };
};