import React, { useEffect } from 'react';
import { Navbar } from '@/components/layout/Navbar';
import { Footer } from '@/components/layout/Footer';
import { HeroSection } from '@/components/sections/HeroSection';
import { ProductGrid } from '@/components/sections/ProductGrid';
import { CategoriesSection } from '@/components/sections/CategoriesSection';
import { useAppDispatch, useAppSelector } from '@/store/hooks';
import { fetchNewestProducts, fetchTopRatedProducts } from '@/store/slices/productsSlice';
import { fetchActiveCategories } from '@/store/slices/categoriesSlice';
import { Skeleton } from '@/components/ui/skeleton';

const Index = () => {
  const dispatch = useAppDispatch();

  // Get data from Redux store
  const { newestProducts, topRatedProducts, status: productsStatus } = useAppSelector(state => state.products);
  const { categories, status: categoriesStatus } = useAppSelector(state => state.categories);

  useEffect(() => {
    // Fetch data on component mount
    dispatch(fetchNewestProducts());
    dispatch(fetchTopRatedProducts());
    dispatch(fetchActiveCategories());
  }, [dispatch]);

  const isLoading = productsStatus === 'loading' || categoriesStatus === 'loading';

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      
      <main>
        <HeroSection />
        
        {/* Categories Section */}
        <section className="py-12">
          <div className="container mx-auto px-4">
            <div className="mb-8">
              <h2 className="text-3xl font-bold text-center mb-4">Shop by Category</h2>
              <p className="text-gray-600 text-center">Discover our wide range of product categories</p>
            </div>
            <CategoriesSection categories={categories} isLoading={categoriesStatus === 'loading'} />
          </div>
        </section>

        {/* Newest Products */}
        <section className="py-16">
          <div className="container mx-auto px-4">
            <div className="mb-8">
              <h2 className="text-3xl font-bold text-center mb-4">Newest Products</h2>
              <p className="text-gray-600 text-center">Check out our latest arrivals</p>
            </div>

            {productsStatus === 'loading' ? (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                {[...Array(4)].map((_, i) => (
                  <div key={i} className="space-y-4">
                    <Skeleton className="aspect-square rounded-lg" />
                    <Skeleton className="h-4 w-3/4" />
                    <Skeleton className="h-4 w-1/2" />
                    <Skeleton className="h-8 w-full" />
                  </div>
                ))}
              </div>
            ) : (
              <ProductGrid products={newestProducts} />
            )}
          </div>
        </section>

        {/* Top Rated Products */}
        <section className="py-16 bg-gray-50">
          <div className="container mx-auto px-4">
            <div className="mb-8">
              <h2 className="text-3xl font-bold text-center mb-4">Top Rated Products</h2>
              <p className="text-gray-600 text-center">Our customers' favorites</p>
            </div>

            {productsStatus === 'loading' ? (
              <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
                {[...Array(4)].map((_, i) => (
                  <div key={i} className="space-y-4">
                    <Skeleton className="aspect-square rounded-lg" />
                    <Skeleton className="h-4 w-3/4" />
                    <Skeleton className="h-4 w-1/2" />
                    <Skeleton className="h-8 w-full" />
                  </div>
                ))}
              </div>
            ) : (
              <ProductGrid products={topRatedProducts} />
            )}
          </div>
        </section>
      </main>
      
      <Footer />
    </div>
  );
};

export default Index;
