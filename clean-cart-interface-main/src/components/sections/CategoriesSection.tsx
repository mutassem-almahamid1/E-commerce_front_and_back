import React from 'react';
import { Link } from 'react-router-dom';
import { ArrowRight } from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Category } from '@/types';
import { Skeleton } from '@/components/ui/skeleton';

interface CategoriesSectionProps {
  categories: Category[];
  isLoading?: boolean;
}

export const CategoriesSection: React.FC<CategoriesSectionProps> = ({
  categories,
  isLoading = false
}) => {
  if (isLoading) {
    return (
      <section className="py-16 bg-muted/30">
        <div className="container mx-auto px-4">
          <div className="text-center mb-12">
            <Skeleton className="h-8 w-64 mx-auto mb-4" />
            <Skeleton className="h-6 w-96 mx-auto" />
          </div>
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 md:gap-6">
            {[...Array(8)].map((_, index) => (
              <div key={index} className="text-center p-6">
                <Skeleton className="w-16 h-16 mx-auto mb-4 rounded-full" />
                <Skeleton className="h-6 w-24 mx-auto mb-2" />
                <Skeleton className="h-4 w-16 mx-auto" />
              </div>
            ))}
          </div>
        </div>
      </section>
    );
  }

  return (
    <section className="py-16 bg-muted/30">
      <div className="container mx-auto px-4">
        {/* Header */}
        <div className="text-center mb-12">
          <h2 className="text-3xl md:text-4xl font-bold text-foreground mb-4">
            Shop by Category
          </h2>
          <p className="text-lg text-muted-foreground max-w-2xl mx-auto">
            Discover our wide range of categories and find exactly what you're looking for
          </p>
        </div>

        {/* Categories Grid */}
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 md:gap-6">
          {categories?.slice(0, 8).map((category, index) => {
            if (!category || !category.categoryName) {
              return null;
            }

            return (
              <Link
                key={category.id || index}
                to={`/products?category=${encodeURIComponent(category.categoryName)}`}
                className="group"
              >
                <div className="card-product text-center p-6 hover-lift animate-fade-in">
                  {/* Category Image/Icon */}
                  <div className="w-16 h-16 mx-auto mb-4 bg-gradient-to-br from-primary/10 to-accent/10 rounded-full flex items-center justify-center group-hover:from-primary/20 group-hover:to-accent/20 transition-all duration-300">
                    {category.categoryImage ? (
                      <img
                        src={category.categoryImage}
                        alt={category.categoryName}
                        className="w-10 h-10 object-contain"
                      />
                    ) : (
                      <span className="text-2xl font-bold text-primary group-hover:text-accent transition-colors">
                        {category.categoryName.charAt(0).toUpperCase()}
                      </span>
                    )}
                  </div>

                  {/* Category Name */}
                  <h3 className="font-semibold text-foreground mb-2 group-hover:text-primary transition-colors">
                    {category.categoryName}
                  </h3>

                  {/* Category Description */}
                  {category.categoryDescription && (
                    <p className="text-sm text-muted-foreground mb-3 line-clamp-2">
                      {category.categoryDescription}
                    </p>
                  )}

                  {/* View Category Button */}
                  <Button
                    variant="ghost"
                    size="sm"
                    className="group-hover:bg-primary/10 transition-colors"
                  >
                    View Category
                    <ArrowRight className="w-4 h-4 ml-1 group-hover:translate-x-1 transition-transform" />
                  </Button>
                </div>
              </Link>
            );
          })}
        </div>

        {/* View All Categories Button */}
        {categories && categories.length > 8 && (
          <div className="text-center mt-12">
            <Button asChild size="lg">
              <Link to="/categories">
                View All Categories
                <ArrowRight className="w-4 h-4 ml-2" />
              </Link>
            </Button>
          </div>
        )}

        {/* Empty State */}
        {(!categories || categories.length === 0) && !isLoading && (
          <div className="text-center py-12">
            <p className="text-muted-foreground text-lg">No categories available at the moment.</p>
          </div>
        )}
      </div>
    </section>
  );
};