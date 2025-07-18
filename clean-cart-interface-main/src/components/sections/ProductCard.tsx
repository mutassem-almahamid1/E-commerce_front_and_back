import React from 'react';
import { Link } from 'react-router-dom';
import { Heart, ShoppingCart, Star, Eye } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Product } from '@/types';
import { useCart } from '@/hooks/useCart';
import { toast } from '@/hooks/use-toast';

interface ProductCardProps {
  product: Product;
}

export const ProductCard: React.FC<ProductCardProps> = ({ product }) => {
  const { addToCart, isInCart, getItemQuantity } = useCart();

  const handleAddToCart = async (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();

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
    }
  };

  const handleWishlist = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();
    toast({
      title: "Added to Wishlist",
      description: `${product.name} has been added to your wishlist`,
    });
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(price);
  };

  const isNew = new Date(product.createdAt) > new Date(Date.now() - 7 * 24 * 60 * 60 * 1000);
  const isLowStock = product.stockQuantity < 10;
  const inCart = isInCart(product.id);
  const cartQuantity = getItemQuantity(product.id);

  return (
    <div className="card-product group cursor-pointer bg-card rounded-lg shadow-sm hover:shadow-md transition-shadow duration-200">
      <Link to={product.id ? `/products/${product.id}` : '#'} className="block">
        {/* Image Container */}
        <div className="relative aspect-square overflow-hidden rounded-t-lg">
          {/* Badges */}
          <div className="absolute top-2 left-2 z-10 flex flex-col gap-1">
            {isNew && <Badge variant="secondary" className="text-xs">New</Badge>}
            {isLowStock && <Badge variant="destructive" className="text-xs">Low Stock</Badge>}
            {product.status === 'SALE' && <Badge variant="default" className="text-xs">Sale</Badge>}
          </div>

          {/* Action Buttons */}
          <div className="absolute top-2 right-2 z-10 flex flex-col gap-1 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
            <Button
              size="sm"
              variant="outline"
              className="w-8 h-8 p-0 bg-white/80 backdrop-blur-sm"
              onClick={handleWishlist}
            >
              <Heart className="w-4 h-4" />
            </Button>
            <Button
              size="sm"
              variant="outline"
              className="w-8 h-8 p-0 bg-white/80 backdrop-blur-sm"
            >
              <Eye className="w-4 h-4" />
            </Button>
          </div>

          <img
            src={product.images && product.images.length > 0 ? product.images[0] : product.image || '/placeholder.svg'}
            alt={product.name}
            className="w-full h-full object-cover transition-transform duration-300 group-hover:scale-105"
            loading="lazy"
            onError={(e) => {
              const target = e.target as HTMLImageElement;
              target.src = '/placeholder.svg';
            }}
          />

          {/* Quick Add to Cart Overlay */}
          <div className="absolute inset-x-0 bottom-0 bg-gradient-to-t from-black/60 to-transparent p-4 opacity-0 group-hover:opacity-100 transition-opacity duration-200">
            <Button
              onClick={handleAddToCart}
              className="w-full"
              variant={inCart ? "secondary" : "default"}
              disabled={product.stockQuantity <= 0}
            >
              <ShoppingCart className="w-4 h-4 mr-2" />
              {product.stockQuantity <= 0
                ? 'Out of Stock'
                : inCart
                  ? `In Cart (${cartQuantity})`
                  : 'Add to Cart'
              }
            </Button>
          </div>
        </div>

        {/* Product Info */}
        <div className="p-4">
          {/* Brand */}
          {product.brand && (
            <p className="text-xs text-muted-foreground uppercase tracking-wide mb-1">
              {product.brand}
            </p>
          )}

          {/* Product Name */}
          <h3 className="font-medium text-foreground mb-2 line-clamp-2 group-hover:text-primary transition-colors">
            {product.name}
          </h3>

          {/* Rating */}
          <div className="flex items-center gap-1 mb-2">
            <div className="flex items-center">
              {[...Array(5)].map((_, i) => (
                <Star
                  key={i}
                  className={`w-3 h-3 ${
                    i < Math.floor(product.avgRating || 0)
                      ? 'text-yellow-400 fill-current'
                      : 'text-gray-300'
                  }`}
                />
              ))}
            </div>
            <span className="text-xs text-muted-foreground ml-1">
              ({product.reviewCount || 0})
            </span>
          </div>

          {/* Price */}
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <span className="font-bold text-foreground">
                {formatPrice(product.price)}
              </span>
            </div>

            {/* Stock Status */}
            <div className="text-xs">
              {product.stockQuantity > 0 ? (
                <span className="text-green-600">
                  {product.stockQuantity} in stock
                </span>
              ) : (
                <span className="text-red-600">Out of stock</span>
              )}
            </div>
          </div>

          {/* Add to Cart Button (Mobile) */}
          <div className="mt-3 block md:hidden">
            <Button
              onClick={handleAddToCart}
              className="w-full"
              variant={inCart ? "secondary" : "default"}
              size="sm"
              disabled={product.stockQuantity <= 0}
            >
              <ShoppingCart className="w-4 h-4 mr-2" />
              {product.stockQuantity <= 0
                ? 'Out of Stock'
                : inCart
                  ? `In Cart (${cartQuantity})`
                  : 'Add to Cart'
              }
            </Button>
          </div>
        </div>
      </Link>
    </div>
  );
};

export default ProductCard;
