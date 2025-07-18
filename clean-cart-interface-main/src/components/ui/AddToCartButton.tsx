import React, { useState } from 'react';
import { ShoppingCart, Plus, Minus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Product } from '@/types';
import { useCart } from '@/hooks/useCart';
import { useAuth } from '@/context/AuthContext';
import { toast } from '@/hooks/use-toast';

interface AddToCartButtonProps {
  product: Product;
  variant?: 'default' | 'outline' | 'secondary';
  size?: 'sm' | 'default' | 'lg';
  showQuantityControls?: boolean;
  className?: string;
}

export const AddToCartButton: React.FC<AddToCartButtonProps> = ({
  product,
  variant = 'default',
  size = 'default',
  showQuantityControls = false,
  className = ''
}) => {
  const { addToCart, removeFromCart, updateQuantity, isInCart, getItemQuantity } = useCart();
  const { user } = useAuth();
  const [loading, setLoading] = useState(false);

  const inCart = isInCart(product.id);
  const cartQuantity = getItemQuantity(product.id);
  const isOutOfStock = product.stockQuantity <= 0;

  const handleAddToCart = async () => {
    if (!user) {
      toast({
        title: "Login Required",
        description: "Please login to add items to cart",
        variant: "destructive",
      });
      return;
    }

    setLoading(true);
    try {
      await addToCart(product, 1);
      toast({
        title: "Added to Cart",
        description: `${product.name} has been added to your cart`,
      });
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to add item to cart. Please try again.",
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  };

  const handleIncrement = async () => {
    if (cartQuantity >= product.stockQuantity) {
      toast({
        title: "Stock Limit",
        description: "Cannot add more items than available in stock",
        variant: "destructive",
      });
      return;
    }

    setLoading(true);
    try {
      await addToCart(product, 1);
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to update cart",
        variant: "destructive",
      });
    } finally {
      setLoading(false);
    }
  };

  const handleDecrement = async () => {
    if (cartQuantity <= 1) {
      setLoading(true);
      try {
        await removeFromCart(product.id);
      } catch (error) {
        toast({
          title: "Error",
          description: "Failed to update cart",
          variant: "destructive",
        });
      } finally {
        setLoading(false);
      }
    } else {
      setLoading(true);
      try {
        await updateQuantity(product.id, cartQuantity - 1);
      } catch (error) {
        toast({
          title: "Error",
          description: "Failed to update cart",
          variant: "destructive",
        });
      } finally {
        setLoading(false);
      }
    }
  };

  if (isOutOfStock) {
    return (
      <Button
        variant="outline"
        size={size}
        disabled
        className={className}
      >
        Out of Stock
      </Button>
    );
  }

  if (inCart && showQuantityControls) {
    return (
      <div className={`flex items-center gap-2 ${className}`}>
        <Button
          variant="outline"
          size="sm"
          onClick={handleDecrement}
          disabled={loading}
          className="w-8 h-8 p-0"
        >
          <Minus className="w-4 h-4" />
        </Button>

        <span className="w-12 text-center font-medium">
          {cartQuantity}
        </span>

        <Button
          variant="outline"
          size="sm"
          onClick={handleIncrement}
          disabled={loading || cartQuantity >= product.stockQuantity}
          className="w-8 h-8 p-0"
        >
          <Plus className="w-4 h-4" />
        </Button>

        <Button
          variant="destructive"
          size="sm"
          onClick={() => removeFromCart(product.id)}
          disabled={loading}
        >
          Remove
        </Button>
      </div>
    );
  }

  return (
    <Button
      variant={inCart ? 'secondary' : variant}
      size={size}
      onClick={handleAddToCart}
      disabled={loading}
      className={className}
    >
      <ShoppingCart className="w-4 h-4 mr-2" />
      {loading ? 'Adding...' : inCart ? `In Cart (${cartQuantity})` : 'Add to Cart'}
    </Button>
  );
};

export default AddToCartButton;
