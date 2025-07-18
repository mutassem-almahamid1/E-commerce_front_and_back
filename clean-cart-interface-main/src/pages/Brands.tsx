import { useQuery } from "@tanstack/react-query";
import { Link } from "react-router-dom";
import { apiClient } from "@/lib/api";
import { Button } from "@/components/ui/button";
import { Card, CardContent } from "@/components/ui/card";
import { Loader2, ExternalLink } from "lucide-react";
import { Navbar } from "@/components/layout/Navbar";
import { Footer } from "@/components/layout/Footer";

const Brands = () => {
  const { data: brands, isLoading } = useQuery({
    queryKey: ['brands'],
    queryFn: () => apiClient.getActiveBrands()
  });

  console.log('brands data:', brands);

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      
      <main className="container mx-auto px-4 py-8">
        <div className="mb-8">
          <h1 className="text-4xl font-bold text-foreground mb-4">All Brands</h1>
          <p className="text-muted-foreground">Discover products from your favorite brands</p>
        </div>

        {isLoading ? (
          <div className="flex justify-center items-center py-12">
            <Loader2 className="h-8 w-8 animate-spin text-primary" />
          </div>
        ) : (
          <>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
              {(Array.isArray(brands) && brands.length > 0) ? brands.map((brand, idx) => (
                <Card key={brand.brandName || brand.name || idx} className="group hover:shadow-lg transition-all duration-300 hover-scale">
                  <CardContent className="p-6">
                    <div className="text-center">
                      {brand.logo || brand.brandImage ? (
                        <img
                          src={brand.logo || brand.brandImage}
                          alt={brand.name || brand.brandName}
                          className="w-20 h-20 mx-auto mb-4 object-contain rounded-lg"
                        />
                      ) : (
                        <div className="w-20 h-20 mx-auto mb-4 bg-gradient-primary rounded-lg flex items-center justify-center">
                          <span className="text-2xl font-bold text-primary-foreground">
                            {(brand.name || brand.brandName || "B").charAt(0)}
                          </span>
                        </div>
                      )}
                      
                      <h3 className="text-xl font-semibold text-foreground mb-2">
                        {brand.name || brand.brandName}
                      </h3>
                      
                      {(brand.description || brand.brandDescription) && (
                        <p className="text-sm text-muted-foreground mb-4 line-clamp-2">
                          {brand.description || brand.brandDescription}
                        </p>
                      )}
                      
                      <Link to={`/products?brand=${encodeURIComponent(brand.name || brand.brandName)}`}>
                        <Button variant="outline" className="w-full group">
                          <span>View Products</span>
                          <ExternalLink className="ml-2 h-4 w-4 group-hover:translate-x-1 transition-transform" />
                        </Button>
                      </Link>
                    </div>
                  </CardContent>
                </Card>
              )) : (
                <div className="text-center py-12 col-span-full">
                  <p className="text-muted-foreground">No brands found.</p>
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

export default Brands;