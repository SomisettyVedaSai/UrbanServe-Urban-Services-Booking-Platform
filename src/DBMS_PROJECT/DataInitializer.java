package DBMS_PROJECT;


import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import java.util.Arrays;
import java.util.List;

public class DataInitializer {
    
    public static void initializeSampleData(MongoDatabase database) {
        // Clear existing collections
        database.getCollection("customers").drop();
        database.getCollection("providers").drop();
        database.getCollection("services").drop();
        database.getCollection("bookings").drop();
        database.getCollection("provider_services").drop();
        database.getCollection("feedback").drop();
        database.getCollection("booking_details").drop();
        
        // Insert Customers
        List<Document> customers = Arrays.asList(
            new Document("name", "John Doe").append("email", "john.doe@email.com")
                .append("phone", "123-456-7890").append("address", "123 Main St, New York, NY"),
            new Document("name", "Jane Smith").append("email", "jane.smith@email.com")
                .append("phone", "123-456-7891").append("address", "456 Oak Ave, Boston, MA"),
            new Document("name", "Mike Johnson").append("email", "mike.johnson@email.com")
                .append("phone", "123-456-7892").append("address", "789 Pine St, Chicago, IL"),
            new Document("name", "Sarah Wilson").append("email", "sarah.wilson@email.com")
                .append("phone", "123-456-7893").append("address", "321 Elm St, Los Angeles, CA"),
            new Document("name", "David Brown").append("email", "david.brown@email.com")
                .append("phone", "123-456-7894").append("address", "654 Maple Dr, Miami, FL")
        );
        database.getCollection("customers").insertMany(customers);
        
        // Insert Providers
        List<Document> providers = Arrays.asList(
            new Document("name", "Quality Home Services").append("email", "quality@email.com")
                .append("phone", "555-1001").append("overall_rating", 4.8),
            new Document("name", "Expert Repair Pros").append("email", "expert@email.com")
                .append("phone", "555-1002").append("overall_rating", 4.6),
            new Document("name", "Quick Fix Solutions").append("email", "quickfix@email.com")
                .append("phone", "555-1003").append("overall_rating", 4.2),
            new Document("name", "Professional Caretakers").append("email", "caretakers@email.com")
                .append("phone", "555-1004").append("overall_rating", 4.9),
            new Document("name", "Reliable Maintenance").append("email", "reliable@email.com")
                .append("phone", "555-1005").append("overall_rating", 4.7)
        );
        database.getCollection("providers").insertMany(providers);
        
        // Insert Services
        List<Document> services = Arrays.asList(
            new Document("name", "Plumbing Repair").append("category", "Home Repair")
                .append("base_price", 80.00).append("description", "Fix leaks and plumbing issues"),
            new Document("name", "Electrical Wiring").append("category", "Home Repair")
                .append("base_price", 120.00).append("description", "Electrical system installation and repair"),
            new Document("name", "House Cleaning").append("category", "Cleaning")
                .append("base_price", 60.00).append("description", "Complete house cleaning service"),
            new Document("name", "AC Repair").append("category", "HVAC")
                .append("base_price", 150.00).append("description", "Air conditioning system repair"),
            new Document("name", "Carpentry").append("category", "Home Repair")
                .append("base_price", 90.00).append("description", "Woodwork and furniture repair"),
            new Document("name", "Painting").append("category", "Home Improvement")
                .append("base_price", 200.00).append("description", "Interior and exterior painting"),
            new Document("name", "Gardening").append("category", "Landscaping")
                .append("base_price", 50.00).append("description", "Garden maintenance and landscaping"),
            new Document("name", "Appliance Repair").append("category", "Home Repair")
                .append("base_price", 100.00).append("description", "Repair of home appliances"),
            new Document("name", "Pest Control").append("category", "Home Care")
                .append("base_price", 130.00).append("description", "Pest elimination and control"),
            new Document("name", "Moving Assistance").append("category", "Moving")
                .append("base_price", 75.00).append("description", "Help with moving and relocation")
        );
        database.getCollection("services").insertMany(services);
        
        // Insert Provider Services
        List<Document> providerServices = Arrays.asList(
            new Document("provider_id", "1").append("service_id", "1")
                .append("experience_level", "Expert").append("price_modifier", 1.2),
            new Document("provider_id", "1").append("service_id", "2")
                .append("experience_level", "Advanced").append("price_modifier", 1.1),
            new Document("provider_id", "2").append("service_id", "3")
                .append("experience_level", "Intermediate").append("price_modifier", 1.0),
            new Document("provider_id", "2").append("service_id", "4")
                .append("experience_level", "Expert").append("price_modifier", 1.3),
            new Document("provider_id", "3").append("service_id", "5")
                .append("experience_level", "Beginner").append("price_modifier", 0.9),
            new Document("provider_id", "3").append("service_id", "6")
                .append("experience_level", "Intermediate").append("price_modifier", 1.0),
            new Document("provider_id", "4").append("service_id", "7")
                .append("experience_level", "Expert").append("price_modifier", 1.2),
            new Document("provider_id", "4").append("service_id", "8")
                .append("experience_level", "Advanced").append("price_modifier", 1.1),
            new Document("provider_id", "5").append("service_id", "9")
                .append("experience_level", "Expert").append("price_modifier", 1.3),
            new Document("provider_id", "5").append("service_id", "10")
                .append("experience_level", "Intermediate").append("price_modifier", 1.0)
        );
        database.getCollection("provider_services").insertMany(providerServices);
        
        // Insert Bookings
        List<Document> bookings = Arrays.asList(
            new Document("customer_id", "1").append("service_id", "1").append("provider_id", "1")
                .append("booking_date", "2024-01-10").append("service_date", "2024-01-15")
                .append("service_time", "09:00").append("duration", "2 hours")
                .append("status", "Completed").append("base_price", 80.00)
                .append("gst", 7.20).append("total_price", 87.20).append("payment_method", "Credit Card"),
            new Document("customer_id", "2").append("service_id", "3").append("provider_id", "2")
                .append("booking_date", "2024-01-12").append("service_date", "2024-01-18")
                .append("service_time", "14:00").append("duration", "3 hours")
                .append("status", "Completed").append("base_price", 60.00)
                .append("gst", 5.40).append("total_price", 65.40).append("payment_method", "PayPal"),
            new Document("customer_id", "3").append("service_id", "5").append("provider_id", "3")
                .append("booking_date", "2024-01-14").append("service_date", "2024-01-20")
                .append("service_time", "10:30").append("duration", "4 hours")
                .append("status", "Pending").append("base_price", 90.00)
                .append("gst", 8.10).append("total_price", 98.10).append("payment_method", "Cash"),
            new Document("customer_id", "4").append("service_id", "7").append("provider_id", "4")
                .append("booking_date", "2024-01-16").append("service_date", "2024-01-22")
                .append("service_time", "11:00").append("duration", "2 hours")
                .append("status", "Confirmed").append("base_price", 50.00)
                .append("gst", 4.50).append("total_price", 54.50).append("payment_method", "Credit Card"),
            new Document("customer_id", "5").append("service_id", "9").append("provider_id", "5")
                .append("booking_date", "2024-01-18").append("service_date", "2024-01-25")
                .append("service_time", "13:00").append("duration", "3 hours")
                .append("status", "Completed").append("base_price", 130.00)
                .append("gst", 11.70).append("total_price", 141.70).append("payment_method", "PayPal")
        );
        database.getCollection("bookings").insertMany(bookings);
        
        // Insert Feedback
        List<Document> feedback = Arrays.asList(
            new Document("booking_id", "1").append("rating", 5)
                .append("comments", "Excellent service, very professional!").append("feedback_date", "2024-01-16"),
            new Document("booking_id", "2").append("rating", 4)
                .append("comments", "Good service but a bit late").append("feedback_date", "2024-01-19"),
            new Document("booking_id", "5").append("rating", 5)
                .append("comments", "Perfect service, highly recommended!").append("feedback_date", "2024-01-26")
        );
        database.getCollection("feedback").insertMany(feedback);
        
        // Insert Booking Details
        List<Document> bookingDetails = Arrays.asList(
            new Document("booking_id", "1").append("special_instructions", "Please ring bell twice")
                .append("service_specific_data", new Document("pipe_type", "copper").append("issue", "leak")),
            new Document("booking_id", "2").append("special_instructions", "Park in driveway")
                .append("service_specific_data", new Document("rooms", 3).append("cleaning_type", "deep")),
            new Document("booking_id", "3").append("special_instructions", "Use back entrance")
                .append("service_specific_data", new Document("furniture_type", "cabinet").append("repair_type", "hinge")),
            new Document("booking_id", "4").append("special_instructions", "Water plants in backyard")
                .append("service_specific_data", new Document("garden_size", "medium").append("services", Arrays.asList("weeding", "pruning"))),
            new Document("booking_id", "5").append("special_instructions", "Treat entire house")
                .append("service_specific_data", new Document("pest_type", "ants").append("infestation_level", "moderate"))
        );
        database.getCollection("booking_details").insertMany(bookingDetails);
        
        System.out.println("Sample data initialized successfully!");
    }
}
