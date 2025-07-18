import { useQuery } from "@tanstack/react-query";
import { Link } from "react-router-dom";
import { apiClient } from "@/lib/api";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Loader2, ExternalLink, Package } from "lucide-react";
import { Navbar } from "@/components/layout/Navbar";
import { Footer } from "@/components/layout/Footer";

const Categories = () => {
  const { data: categories, isLoading } = useQuery({
    queryKey: ['categories'],
    queryFn: () => apiClient.getActiveCategories()
  });

  console.log('categories data:', categories);

  const getCategoryIcon = (categoryName?: string) => {
    if (!categoryName) return '📦';
    const name = categoryName.toLowerCase();
    if (name.includes('electronics') || name.includes('tech')) return '📱';
    if (name.includes('clothing') || name.includes('fashion')) return '👕';
    if (name.includes('home') || name.includes('furniture')) return '🏠';
    if (name.includes('book')) return '📚';
    if (name.includes('sport') || name.includes('fitness')) return '⚽';
    if (name.includes('beauty') || name.includes('health')) return '💄';
    if (name.includes('food') || name.includes('grocery')) return '🍎';
    if (name.includes('automotive') || name.includes('car')) return '🚗';
    return '📦';
  };

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      
      <main className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-foreground mb-4">All Categories</h1>
          <p className="text-muted-foreground">Browse products by category</p>
        </div>

        {isLoading ? (
          <div className="flex justify-center items-center py-12">
            <Loader2 className="h-8 w-8 animate-spin text-primary" />
          </div>
        ) : (
          <>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {(Array.isArray(categories) && categories.length > 0) ? categories.map((category, idx) => (
                <Card key={`category-${category.categoryName || 'unnamed'}-${idx}`} className="group hover:shadow-lg transition-all duration-300 hover-scale">
                  <CardContent className="p-6">
                    <div className="text-center">
                      {category.categoryImage ? (
                        <img
                          src={category.categoryImage}
                          alt={category.categoryName}
                          className="w-20 h-20 mx-auto mb-4 object-cover rounded-lg"
                        />
                      ) : (
                        <div className="w-20 h-20 mx-auto mb-4 bg-gradient-subtle rounded-lg flex items-center justify-center text-4xl">
                          {getCategoryIcon(category.categoryName)}
                        </div>
                      )}
                      
                      <h3 className="text-xl font-semibold text-foreground mb-2">
                        {category.categoryName}
                      </h3>
                      
                      {category.categoryDescription && (
                        <p className="text-sm text-muted-foreground mb-4 line-clamp-2">
                          {category.categoryDescription}
                        </p>
                      )}
                      
                      <Link to={`/products?category=${encodeURIComponent(category.categoryName)}`}>
                        <Button variant="outline" className="w-full group">
                          <Package className="mr-2 h-4 w-4" />
                          <span>Browse Products</span>
                          <ExternalLink className="ml-2 h-4 w-4 group-hover:translate-x-1 transition-transform" />
                        </Button>
                      </Link>
                    </div>
                  </CardContent>
                </Card>
              )) : (
                <div className="text-center py-12 col-span-full">
                  <p className="text-muted-foreground">No categories found.</p>
                </div>
              )}
            </div>
          </>
        )}
      </main>

      <Footer />
    </div>
  );
};

export default Categories;