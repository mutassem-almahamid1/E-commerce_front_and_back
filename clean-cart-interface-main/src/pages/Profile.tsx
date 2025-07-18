import { Navbar } from "@/components/layout/Navbar";
import { Footer } from "@/components/layout/Footer";
import { useAuth } from "@/context/AuthContext";
import { Button } from "@/components/ui/button";

const Profile = () => {
  const { user, isAuthenticated, isLoading } = useAuth();

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <main className="container mx-auto px-4 py-8">
        <h1 className="text-4xl font-bold text-foreground mb-4">Profile</h1>
        <p className="text-muted-foreground mb-8">View and edit your profile information.</p>
        {isLoading ? (
          <div className="text-center text-muted-foreground">Loading...</div>
        ) : !isAuthenticated || !user ? (
          <div className="text-center text-muted-foreground">You are not logged in.</div>
        ) : (
          <div className="max-w-md mx-auto bg-card p-6 rounded-lg shadow">
            <div className="mb-4">
              <span className="font-semibold">Name:</span> {user.name}
            </div>
            <div className="mb-4">
              <span className="font-semibold">Email:</span> {user.email}
            </div>
            {/* يمكنك إضافة المزيد من بيانات المستخدم هنا */}
            <Button variant="outline" className="mt-4 w-full">Edit Profile</Button>
          </div>
        )}
      </main>
      <Footer />
    </div>
  );
};

export default Profile;
