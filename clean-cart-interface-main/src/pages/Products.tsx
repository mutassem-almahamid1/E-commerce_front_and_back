import { useState, useEffect } from "react";
import { useQuery } from "@tanstack/react-query";
import { useLocation } from "react-router-dom";
import { apiClient } from "@/lib/api";
import { ProductCard } from "@/components/sections/ProductCard";
import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { Loader2, Search } from "lucide-react";
import { Navbar } from "@/components/layout/Navbar";
import { Footer } from "@/components/layout/Footer";

const Products = () => {
  const [page, setPage] = useState(0);
  const [searchQuery, setSearchQuery] = useState("");
  const [selectedCategory, setSelectedCategory] = useState<string>("");
  const [selectedBrand, setSelectedBrand] = useState<string>("");

  // Obtener parámetros de la URL
  const location = useLocation();
  const queryParams = new URLSearchParams(location.search);
  const categoryParam = queryParams.get('category');

  useEffect(() => {
    // Si hay un parámetro de categoría en la URL, establecerlo
    if (categoryParam) {
      setSelectedCategory(categoryParam);
    }
  }, [categoryParam]);

  const { data: productsData, isLoading: productsLoading } = useQuery({
    queryKey: ['products', page, searchQuery, selectedCategory, selectedBrand],
    queryFn: () => {
      if (searchQuery) {
        return apiClient.searchProducts(searchQuery, page, 12);
      }
      if (selectedCategory) {
        return apiClient.getProductsByCategory(selectedCategory, page, 12);
      }
      if (selectedBrand) {
        return apiClient.getProductsByBrand(selectedBrand, page, 12);
      }
      return apiClient.getProducts(page, 12);
    }
  });

  const { data: categories } = useQuery({
    queryKey: ['categories'],
    queryFn: () => apiClient.getActiveCategories()
  });

  const { data: brandsData } = useQuery({
    queryKey: ['brands'],
    queryFn: () => apiClient.getActiveBrands()
  });

  console.log('categories:', categories);
  console.log('brandsData:', brandsData);
  console.log('productsData:', productsData);

  const handleSearch = () => {
    setPage(0);
    setSelectedCategory("");
    setSelectedBrand("");
  };

  const handleCategoryChange = (value: string) => {
    setSelectedCategory(value === "all" ? "" : value);
    setSearchQuery("");
    setSelectedBrand("");
    setPage(0);
  };

  const handleBrandChange = (value: string) => {
    setSelectedBrand(value === "all" ? "" : value);
    setSearchQuery("");
    setSelectedCategory("");
    setPage(0);
  };

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      
      <main className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-foreground mb-6">All Products</h1>
          
          {/* Filters */}
          <div className="flex flex-col md:flex-row gap-4 mb-6">
            <div className="flex-1">
              <div className="relative">
                <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
                <Input
                  placeholder="Search products..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="pl-10"
                  onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                />
              </div>
            </div>
            
            <Button onClick={handleSearch} className="md:w-auto">
              Search
            </Button>
            
            <Select value={selectedCategory} onValueChange={handleCategoryChange}>
              <SelectTrigger className="md:w-48">
                <SelectValue placeholder="All Categories" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all" key="all-categories">All Categories</SelectItem>
                {(Array.isArray(categories) && categories.length > 0) ? (
                  categories.map((category) => (
                    <SelectItem
                      key={category.categoryName}
                      value={category.categoryName}
                    >
                      {category.categoryName}
                    </SelectItem>
                  ))
                ) : (
                  <SelectItem value="none" key="none-cat" disabled>No categories found</SelectItem>
                )}
              </SelectContent>
            </Select>
            <Select value={selectedBrand} onValueChange={handleBrandChange}>
              <SelectTrigger className="md:w-48">
                <SelectValue placeholder="All Brands" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all" key="all-brands">All Brands</SelectItem>
                {(Array.isArray(brandsData) && brandsData.length > 0) ? (
                  brandsData.map((brand) => (
                    <SelectItem
                      key={brand.brandName || brand.name}
                      value={brand.brandName || brand.name}
                    >
                      {brand.brandName || brand.name}
                    </SelectItem>
                  ))
                ) : (
                  <SelectItem value="none" key="none-brand" disabled>No brands found</SelectItem>
                )}
              </SelectContent>
            </Select>
          </div>
        </div>

        {/* Products Grid */}
        {productsLoading ? (
          <div className="flex justify-center items-center py-12">
            <Loader2 className="h-8 w-8 animate-spin text-primary" />
          </div>
        ) : (
          <>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {productsData?.content?.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))}
            </div>

            {/* Pagination */}
            {productsData && productsData.page && productsData.page.totalPages > 1 && (
              <div className="flex justify-center items-center gap-4 mt-8">
                <Button
                  variant="outline"
                  onClick={() => setPage(page - 1)}
                  disabled={page === 0}
                >
                  Previous
                </Button>
                <span className="text-muted-foreground">
                  Page {page + 1} of {productsData.page.totalPages}
                </span>
                <Button
                  variant="outline"
                  onClick={() => setPage(page + 1)}
                  disabled={page >= productsData.page.totalPages - 1}
                >
                  Next
                </Button>
              </div>
            )}

            {productsData?.content?.length === 0 && (
              <div className="text-center py-12">
                <p className="text-muted-foreground">No products found. Try different search criteria.</p>
              </div>
            )}
          </>
        )}
      </main>

      <Footer />
    </div>
  );
};

export default Products;
