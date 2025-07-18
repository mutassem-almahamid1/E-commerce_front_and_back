import React, { useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { Star, Heart, ShoppingCart, Share2, ChevronLeft, Plus, Minus } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Badge } from '@/components/ui/badge';
import { Separator } from '@/components/ui/separator';
import { Card, CardContent } from '@/components/ui/card';
import { Label } from '@/components/ui/label';
import { Textarea } from '@/components/ui/textarea';
import { Navbar } from '@/components/layout/Navbar';
import { Footer } from '@/components/layout/Footer';
import { apiClient } from '@/lib/api';
import { useCart } from '@/hooks/useCart';
import { useAuth } from '@/context/AuthContext';
import { toast } from '@/hooks/use-toast';

const ProductDetails: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [quantity, setQuantity] = useState(1);
  const [selectedImage, setSelectedImage] = useState(0);
  const [userRating, setUserRating] = useState(0);
  const [userComment, setUserComment] = useState('');
  const [hoveredRating, setHoveredRating] = useState(0);
  const [showReviewForm, setShowReviewForm] = useState(false);
  const { addToCart, isInCart, loading: cartLoading } = useCart();
  const { isAuthenticated, user } = useAuth();
  const queryClient = useQueryClient();

  const { data: product, isLoading, error } = useQuery({
    queryKey: ['product', id],
    queryFn: () => apiClient.getProductById(id!),
    enabled: !!id,
  });

  const { data: reviews = [] } = useQuery({
    queryKey: ['reviews', id],
    queryFn: () => apiClient.getReviewsByProductId(id!),
    enabled: !!id,
  });

  // Mutación para enviar una nueva reseña
  const submitReviewMutation = useMutation({
    mutationFn: (reviewData: { rating: number; comment: string }) => {
      if (!user || !product) throw new Error("User or product not found");

      return apiClient.submitReview({
        productId: product.id,
        userId: user.id,
        rating: reviewData.rating,
        comment: reviewData.comment
      });
    },
    onSuccess: () => {
      // Invalidar la consulta de reseñas para تحديث la lista
      queryClient.invalidateQueries({ queryKey: ['reviews', id] });
      queryClient.invalidateQueries({ queryKey: ['product', id] });

      toast({
        title: "Review Submitted Successfully!",
        description: "Thank you for your feedback!",
      });

      // مسح النموذج بعد الإرسال الناجح
      setUserRating(0);
      setUserComment('');
      setHoveredRating(0);
      setShowReviewForm(false);
    },
    onError: (error) => {
      console.error("Error submitting review:", error);
      toast({
        title: "Error",
        description: "Failed to submit review. Please try again.",
        variant: "destructive",
      });
    }
  });

  // تحديث دالة إرسال المراجعة
  const handleSubmitReview = () => {
    if (userRating === 0 || userComment.length < 10) {
      toast({
        title: "Invalid Review",
        description: "Please provide a rating and at least 10 characters in your comment.",
        variant: "destructive",
      });
      return;
    }

    submitReviewMutation.mutate({
      rating: userRating,
      comment: userComment
    });
  };

  const handleAddToCart = async () => {
    if (!product) {
      toast({
        title: "Error",
        description: "Product data not available",
        variant: "destructive",
      });
      return;
    }

    if (!isAuthenticated) {
      toast({
        title: "Login Required",
        description: "Please login to add items to cart",
        variant: "destructive",
      });
      return;
    }

    if (product.stockQuantity < quantity) {
      toast({
        title: "Insufficient Stock",
        description: `Only ${product.stockQuantity} items available`,
        variant: "destructive",
      });
      return;
    }

    try {
      await addToCart(product, quantity);
      toast({
        title: "Added to Cart",
        description: `${product.name} has been added to your cart`,
      });
      setQuantity(1); // Reset quantity after adding
    } catch (error) {
      toast({
        title: "Error",
        description: "Failed to add item to cart. Please try again.",
        variant: "destructive",
      });
    }
  };

  const handleWishlist = () => {
    toast({
      title: "Added to Wishlist",
      description: `${product?.name} has been added to your wishlist`,
    });
  };

  const handleShare = async () => {
    if (navigator.share) {
      try {
        await navigator.share({
          title: product?.name,
          text: product?.description,
          url: window.location.href,
        });
      } catch (error) {
        console.log('Error sharing:', error);
      }
    } else {
      navigator.clipboard.writeText(window.location.href);
      toast({
        title: "Link Copied",
        description: "Product link has been copied to clipboard",
      });
    }
  };

  const formatPrice = (price: number) => {
    return new Intl.NumberFormat('en-US', {
      style: 'currency',
      currency: 'USD',
    }).format(price);
  };

  if (isLoading) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <div className="container mx-auto px-4 py-8">
          <div className="animate-pulse">
            <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
              <div className="space-y-4">
                <div className="aspect-square bg-muted rounded-lg"></div>
                <div className="grid grid-cols-4 gap-2">
                  {[...Array(4)].map((_, i) => (
                    <div key={i} className="aspect-square bg-muted rounded"></div>
                  ))}
                </div>
              </div>
              <div className="space-y-6">
                <div className="h-8 bg-muted rounded w-3/4"></div>
                <div className="h-6 bg-muted rounded w-1/2"></div>
                <div className="h-12 bg-muted rounded w-1/4"></div>
                <div className="space-y-2">
                  <div className="h-4 bg-muted rounded"></div>
                  <div className="h-4 bg-muted rounded w-5/6"></div>
                  <div className="h-4 bg-muted rounded w-4/6"></div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }

  if (error || !product) {
    return (
      <div className="min-h-screen bg-background">
        <Navbar />
        <div className="container mx-auto px-4 py-8">
          <div className="text-center">
            <h1 className="text-2xl font-bold mb-4">Product Not Found</h1>
            <p className="text-muted-foreground mb-6">The product you're looking for doesn't exist.</p>
            <Button variant="premium" asChild>
              <Link to="/products">Back to Products</Link>
            </Button>
          </div>
        </div>
      </div>
    );
  }

  const images = product.images && product.images.length > 0 ? product.images : [product.image];
  const isNew = new Date(product.createdAt) > new Date(Date.now() - 7 * 24 * 60 * 60 * 1000);
  const isLowStock = product.stock < 10;

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      
      <main className="container mx-auto px-4 py-8">
        {/* Breadcrumb */}
        <div className="flex items-center space-x-2 text-sm text-muted-foreground mb-8">
          <Link to="/" className="hover:text-primary transition-colors">Home</Link>
          <span>/</span>
          <Link to="/products" className="hover:text-primary transition-colors">Products</Link>
          <span>/</span>
          <Link to={`/categories/${product.category?.categoryName}`} className="hover:text-primary transition-colors">
            {product.category?.categoryName}
          </Link>
          <span>/</span>
          <span className="text-foreground">{product.name}</span>
        </div>

        {/* Back Button */}
        <Button variant="ghost" className="mb-6" asChild>
          <Link to="/products">
            <ChevronLeft className="mr-2 h-4 w-4" />
            Back to Products
          </Link>
        </Button>

        <div className="grid grid-cols-1 lg:grid-cols-2 gap-12">
          {/* Product Images */}
          <div className="space-y-4">
            {/* Main Image */}
            <div className="aspect-square overflow-hidden rounded-lg border">
              <img
                src={images[selectedImage]}
                alt={product.name}
                className="w-full h-full object-cover"
              />
            </div>

            {/* Thumbnail Images */}
            {images.length > 1 && (
              <div className="grid grid-cols-4 gap-2">
                {images.map((image, index) => (
                  <button
                    key={index}
                    onClick={() => setSelectedImage(index)}
                    className={`aspect-square overflow-hidden rounded border-2 transition-colors ${
                      selectedImage === index ? 'border-primary' : 'border-border hover:border-primary/50'
                    }`}
                  >
                    <img
                      src={image}
                      alt={`${product.name} ${index + 1}`}
                      className="w-full h-full object-cover"
                    />
                  </button>
                ))}
              </div>
            )}
          </div>

          {/* Product Information */}
          <div className="space-y-6">
            {/* Badges */}
            <div className="flex flex-wrap gap-2">
              {isNew && <Badge variant="default" className="bg-accent">New</Badge>}
              {isLowStock && <Badge variant="destructive">Low Stock</Badge>}
              {(product.rating || 0) >= 4.5 && <Badge variant="outline">⭐ Top Rated</Badge>}
            </div>

            {/* Brand */}
            {product.brand && (
              <Link
                to={`/brands/${product.brand.name}`}
                className="text-sm text-muted-foreground hover:text-primary transition-colors"
              >
                {product.brand.name}
              </Link>
            )}

            {/* Product Name */}
            <h1 className="text-3xl font-bold text-foreground">{product.name}</h1>

            {/* Rating */}
            <div className="flex items-center space-x-4">
              <div className="flex items-center">
                {[...Array(5)].map((_, i) => (
                  <Star
                    key={i}
                    className={`h-5 w-5 ${
                      i < Math.floor(product.rating || 0)
                        ? 'fill-accent text-accent'
                        : 'text-muted-foreground'
                    }`}
                  />
                ))}
              </div>
              <span className="text-sm text-muted-foreground">
                {(product.rating || 0).toFixed(1)} ({reviews.length} reviews)
              </span>
            </div>

            {/* Price */}
            <div className="text-3xl font-bold text-foreground">
              {formatPrice(product.price)}
            </div>

            {/* Description */}
            <div>
              <h3 className="font-semibold mb-2">Description</h3>
              <p className="text-muted-foreground leading-relaxed">{product.description}</p>
            </div>

            {/* Stock Status */}
            <div className="flex items-center space-x-2">
              <div className={`w-3 h-3 rounded-full ${(product.stock || 0) > 0 ? 'bg-success' : 'bg-destructive'}`}></div>
              <span className="text-sm">
                {(product.stock || 0) > 0 ? `${product.stock} in stock` : 'Out of stock'}
              </span>
            </div>

            {/* Quantity Selector */}
            <div className="flex items-center space-x-4">
              <span className="font-medium">Quantity:</span>
              <div className="flex items-center border rounded">
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={() => setQuantity(Math.max(1, quantity - 1))}
                  disabled={quantity <= 1}
                  className="h-10 w-10"
                >
                  <Minus className="h-4 w-4" />
                </Button>
                <span className="px-4 py-2 min-w-[3rem] text-center">{quantity}</span>
                <Button
                  variant="ghost"
                  size="icon"
                  onClick={() => setQuantity(Math.min(product.stock || 0, quantity + 1))}
                  disabled={quantity >= (product.stock || 0)}
                  className="h-10 w-10"
                >
                  <Plus className="h-4 w-4" />
                </Button>
              </div>
            </div>

            {/* Action Buttons */}
            <div className="flex flex-col sm:flex-row gap-4">
              <Button
                variant={isInCart(product.id) ? "success" : "premium"}
                size="lg"
                className="flex-1"
                onClick={handleAddToCart}
                disabled={(product.stock || 0) === 0}
              >
                <ShoppingCart className="mr-2 h-5 w-5" />
                {(product.stock || 0) === 0 ? 'Out of Stock' : isInCart(product.id) ? 'Added to Cart' : 'Add to Cart'}
              </Button>
              
              <Button variant="outline" size="lg" onClick={handleWishlist}>
                <Heart className="mr-2 h-4 w-4" />
                Wishlist
              </Button>
              
              <Button variant="outline" size="lg" onClick={handleShare}>
                <Share2 className="mr-2 h-4 w-4" />
                Share
              </Button>
            </div>

            {/* Specifications */}
            {product.specifications && Object.keys(product.specifications).length > 0 && (
              <Card>
                <CardContent className="p-6">
                  <h3 className="font-semibold mb-4">Specifications</h3>
                  <div className="space-y-3">
                    {Object.entries(product.specifications).map(([key, value]) => (
                      <div key={key} className="flex justify-between">
                        <span className="text-muted-foreground">{key}:</span>
                        <span className="font-medium">{value}</span>
                      </div>
                    ))}
                  </div>
                </CardContent>
              </Card>
            )}
          </div>
        </div>

        {/* Reviews Section */}
        <Separator className="my-12" />
        <div className="space-y-8">
          <div className="flex justify-between items-center">
            <h2 className="text-2xl font-bold">Customer Reviews</h2>
            {!isAuthenticated && (
              <p className="text-sm text-muted-foreground">
                <Link to="/login" className="text-primary hover:underline">
                  Login
                </Link>
                {" "}to write a review
              </p>
            )}
            {isAuthenticated && !showReviewForm && (
              <Button variant="premium" onClick={() => setShowReviewForm(true)}>
                اكتب مراجعة
              </Button>
            )}
          </div>

          {/* نموذج كتابة المراجعة - يظهر فقط عند الضغط على الزر */}
          {showReviewForm && (
            <Card className={`border-2 ${isAuthenticated ? 'bg-muted/30 border-green-200' : 'bg-red-50 border-red-200'}`}>
              <CardContent className="p-6">
                <div className="flex justify-between items-center mb-4">
                  <h3 className="text-lg font-semibold">📝 Write a Review</h3>
                  {!isAuthenticated && (
                    <Badge variant="destructive">Login Required</Badge>
                  )}
                  {isAuthenticated && (
                    <Badge variant="default" className="bg-green-500">Ready to Review</Badge>
                  )}
                </div>

                {!isAuthenticated && (
                  <div className="mb-6 p-4 bg-blue-50 border border-blue-200 rounded-lg">
                    <p className="text-sm text-blue-800 text-center">
                      🔐 You need to{" "}
                      <Link to="/login" className="font-semibold text-blue-600 hover:underline">
                        login
                      </Link>
                      {" "}or{" "}
                      <Link to="/signup" className="font-semibold text-blue-600 hover:underline">
                        create an account
                      </Link>
                      {" "}to write a review for this product.
                    </p>
                  </div>
                )}

                {/* نموذج التقييم والمراجعة */}
                <div className="space-y-6">
                  {/* تقييم النجوم */}
                  <div>
                    <Label className="text-base font-medium mb-3 block">⭐ Rating</Label>
                    <div className="flex items-center space-x-2">
                      {[1, 2, 3, 4, 5].map((star) => (
                        <button
                          key={star}
                          type="button"
                          onClick={() => isAuthenticated && setUserRating(star)}
                          onMouseEnter={() => setHoveredRating(star)}
                          onMouseLeave={() => setHoveredRating(0)}
                          className={`focus:outline-none transition-all duration-200 ${
                            !isAuthenticated ? 'cursor-not-allowed opacity-50' : 'cursor-pointer hover:scale-110'
                          }`}
                          disabled={!isAuthenticated}
                        >
                          <Star
                            className={`h-10 w-10 transition-colors ${
                              star <= (hoveredRating || userRating)
                                ? 'text-yellow-400 fill-yellow-400'
                                : 'text-gray-300 hover:text-yellow-200'
                            } ${!isAuthenticated ? 'opacity-50' : ''}`}
                          />
                        </button>
                      ))}
                    </div>
                    {userRating > 0 && isAuthenticated && (
                      <p className="text-sm text-green-600 mt-2 font-medium">
                        ✅ You selected {userRating} star{userRating > 1 ? 's' : ''}
                      </p>
                    )}
                    {!isAuthenticated && (
                      <p className="text-sm text-red-600 mt-2">
                        🔒 Please login to rate this product
                      </p>
                    )}
                  </div>

                  {/* مربع التعليق */}
                  <div>
                    <Label htmlFor="review-comment" className="text-base font-medium mb-3 block">
                      💬 Your Review
                    </Label>
                    <Textarea
                      id="review-comment"
                      placeholder={isAuthenticated
                        ? "Share your thoughts about this product... (minimum 10 characters)"
                        : "Please login to write a review..."
                      }
                      rows={5}
                      value={userComment}
                      onChange={(e) => isAuthenticated && setUserComment(e.target.value)}
                      className={`resize-none border-2 ${
                        !isAuthenticated 
                          ? 'opacity-50 cursor-not-allowed bg-gray-100 border-gray-300' 
                          : ''
                      }`}
                      disabled={!isAuthenticated}
                    />
                  </div>

                  {/* زر الإرسال وإغلاق النموذج */}
                  <div className="flex gap-4">
                    <Button
                      variant="premium"
                      onClick={handleSubmitReview}
                      disabled={!isAuthenticated}
                    >
                      Submit Review
                    </Button>
                    <Button
                      variant="outline"
                      onClick={() => setShowReviewForm(false)}
                    >
                      إلغاء
                    </Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          )}

          {/* قائمة المراجعات */}
          {reviews.length > 0 ? (
            <div className="space-y-6">
              <h3 className="text-lg font-semibold">Customer Reviews ({reviews.length})</h3>
              {reviews.map((review) => (
                <Card key={review.id} className="border-l-4 border-l-accent/30">
                  <CardContent className="p-6">
                    <div className="flex items-start justify-between mb-4">
                      <div>
                        <div className="font-semibold text-foreground">
                          {review.user?.username || "Anonymous User"}
                        </div>
                        <div className="flex items-center mt-2">
                          {[...Array(5)].map((_, i) => (
                            <Star
                              key={i}
                              className={`h-4 w-4 ${
                                i < review.rating
                                  ? 'fill-yellow-400 text-yellow-400'
                                  : 'text-muted-foreground'
                              }`}
                            />
                          ))}
                          <span className="ml-2 text-sm text-muted-foreground">
                            {review.rating}/5
                          </span>
                        </div>
                      </div>
                      <span className="text-sm text-muted-foreground">
                        {new Date(review.createdAt).toLocaleDateString('en-US', {
                          year: 'numeric',
                          month: 'short',
                          day: 'numeric'
                        })}
                      </span>
                    </div>
                    <p className="text-foreground leading-relaxed">{review.comment}</p>
                  </CardContent>
                </Card>
              ))}
            </div>
          ) : (
            <div className="text-center py-12 bg-muted/30 rounded-lg border-2 border-dashed border-muted-foreground/30">
              <div className="text-4xl mb-4">📝</div>
              <h3 className="text-lg font-semibold mb-2">No reviews yet</h3>
              <p className="text-muted-foreground mb-4">
                Be the first to share your experience with this product!
              </p>
              {!isAuthenticated && (
                <Button variant="outline" asChild>
                  <Link to="/login">Login to Write Review</Link>
                </Button>
              )}
            </div>
          )}
        </div>
      </main>


      <Footer />
    </div>
  );
};

export default ProductDetails;
