package DBMS_PROJECT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import java.util.logging.Logger;
import java.util.logging.Level;
public class ServiceBookingApp {
    private static MongoDatabase database;
    private static Scanner scanner = new Scanner(System.in);
    static {
        // Disable MongoDB logging completely
        Logger.getLogger("org.mongodb.driver").setLevel(Level.OFF);
    }
    public static void main(String[] args) {
        database = MongoDBConnection.connect();
        if (database == null) {
            System.out.println("Failed to connect to database. Exiting...");
            return;
        }
        
        // Initialize sample data
        System.out.print("Do you want to initialize sample data? (yes/no): ");
        String initChoice = scanner.nextLine();
        if (initChoice.equalsIgnoreCase("yes")) {
            DataInitializer.initializeSampleData(database);
        }
        
        showMainMenu();
        MongoDBConnection.closeConnection();
    }
    
    private static void showMainMenu() {
        while (true) {
            System.out.println("\n=== SERVICE BOOKING SYSTEM ===");
            System.out.println("1. Collection Operations (Add/Delete/Modify)");
            System.out.println("2. Sample Queries");
            System.out.println("3. Sample Subqueries");
            System.out.println("4. Sample Functions");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline
            
            switch (choice) {
                case 1:
                    collectionOperationsMenu();
                    break;
                case 2:
                    sampleQueries();
                    break;
                case 3:
                    sampleSubqueries();
                    break;
                case 4:
                    proceduralProgramming();
                    break;
                case 5:
                    System.out.println("Exiting... Goodbye!");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    private static void collectionOperationsMenu() {
        while (true) {
            System.out.println("\n=== COLLECTION OPERATIONS ===");
            System.out.println("1. CUSTOMERS - Add/Delete/Modify");
            System.out.println("2. PROVIDERS - Add/Delete/Modify");
            System.out.println("3. SERVICES - Add/Delete/Modify");
            System.out.println("4. BOOKINGS - Add/Delete/Modify");
            System.out.println("5. PROVIDER_SERVICES - Add/Delete/Modify");
            System.out.println("6. FEEDBACK - Add/Delete/Modify");
            System.out.println("7. BOOKING_DETAILS - Add/Delete/Modify");
            System.out.println("8. View All Collections");
            System.out.println("9. Back to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            switch (choice) {
                case 1: manageCustomers(); break;
                case 2: manageProviders(); break;
                case 3: manageServices(); break;
                case 4: manageBookings(); break;
                case 5: manageProviderServices(); break;
                case 6: manageFeedback(); break;
                case 7: manageBookingDetails(); break;
                case 8: viewAllCollections(); break;
                case 9: return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
    
    // Helper method to safely handle ID input (both ObjectId and string)
    private static Object parseId(String id) {
        try {
            // Try to parse as ObjectId first
            if (ObjectId.isValid(id)) {
                return new ObjectId(id);
            }
            // If not a valid ObjectId, use as string
            return id;
        } catch (Exception e) {
            return id; // Return as string if any error
        }
    }
    
    // Collection Management Methods - FIXED VERSIONS
    
    private static void manageCustomers() {
        while (true) {
            System.out.println("\n=== CUSTOMERS MANAGEMENT ===");
            System.out.println("1. Add Customer");
            System.out.println("2. Delete Customer");
            System.out.println("3. Modify Customer");
            System.out.println("4. View All Customers");
            System.out.println("5. Back to Collection Menu");
            System.out.print("Choose: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            MongoCollection<Document> collection = database.getCollection("customers");
            
            switch (choice) {
                case 1:
                    System.out.print("Name: "); String name = scanner.nextLine();
                    System.out.print("Email: "); String email = scanner.nextLine();
                    System.out.print("Phone: "); String phone = scanner.nextLine();
                    System.out.print("Address: "); String address = scanner.nextLine();
                    
                    Document customer = new Document()
                        .append("name", name)
                        .append("email", email)
                        .append("phone", phone)
                        .append("address", address);
                    
                    InsertOneResult result = collection.insertOne(customer);
                    System.out.println("Customer added with ID: " + result.getInsertedId());
                    break;
                    
                case 2:
                    System.out.print("Enter Customer ID to delete: ");
                    String custId = scanner.nextLine();
                    try {
                        Object id = parseId(custId);
                        DeleteResult delResult = collection.deleteOne(Filters.eq("_id", id));
                        if (delResult.getDeletedCount() > 0) {
                            System.out.println("Customer deleted successfully!");
                        } else {
                            System.out.println("Customer not found with ID: " + custId);
                        }
                    } catch (Exception e) {
                        System.out.println("Error deleting customer: " + e.getMessage());
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter Customer ID to modify: ");
                    String modCustId = scanner.nextLine();
                    try {
                        Object id = parseId(modCustId);
                        Document existing = collection.find(Filters.eq("_id", id)).first();
                        if (existing == null) {
                            System.out.println("Customer not found with ID: " + modCustId);
                            break;
                        }
                        
                        System.out.println("Current Details:");
                        System.out.println("Name: " + existing.getString("name"));
                        System.out.println("Email: " + existing.getString("email"));
                        System.out.println("Phone: " + existing.getString("phone"));
                        System.out.println("Address: " + existing.getString("address"));
                        System.out.println("\nEnter new details (press Enter to keep current value):");
                        
                        System.out.print("New Name: "); 
                        String newName = scanner.nextLine();
                        System.out.print("New Email: "); 
                        String newEmail = scanner.nextLine();
                        System.out.print("New Phone: "); 
                        String newPhone = scanner.nextLine();
                        System.out.print("New Address: "); 
                        String newAddress = scanner.nextLine();
                        
                        UpdateResult updateResult = collection.updateOne(
                            Filters.eq("_id", id),
                            Updates.combine(
                                Updates.set("name", newName.isEmpty() ? existing.getString("name") : newName),
                                Updates.set("email", newEmail.isEmpty() ? existing.getString("email") : newEmail),
                                Updates.set("phone", newPhone.isEmpty() ? existing.getString("phone") : newPhone),
                                Updates.set("address", newAddress.isEmpty() ? existing.getString("address") : newAddress)
                            )
                        );
                        if (updateResult.getModifiedCount() > 0) {
                            System.out.println("Customer modified successfully!");
                        } else {
                            System.out.println("No changes made to customer.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error modifying customer: " + e.getMessage());
                    }
                    break;
                    
                case 4:
                    System.out.println("\nAll Customers:");
                    int customerCount = 0;
                    for (Document doc : collection.find()) {
                        System.out.println("ID: " + doc.get("_id") + 
                                         ", Name: " + doc.getString("name") +
                                         ", Email: " + doc.getString("email") +
                                         ", Phone: " + doc.getString("phone"));
                        customerCount++;
                    }
                    if (customerCount == 0) {
                        System.out.println("No customers found.");
                    }
                    break;
                    
                case 5:
                    return;
                    
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void manageProviders() {
        while (true) {
            System.out.println("\n=== PROVIDERS MANAGEMENT ===");
            System.out.println("1. Add Provider");
            System.out.println("2. Delete Provider");
            System.out.println("3. Modify Provider");
            System.out.println("4. View All Providers");
            System.out.println("5. Back to Collection Menu");
            System.out.print("Choose: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            MongoCollection<Document> collection = database.getCollection("providers");
            
            switch (choice) {
                case 1:
                    System.out.print("Name: "); String name = scanner.nextLine();
                    System.out.print("Email: "); String email = scanner.nextLine();
                    System.out.print("Phone: "); String phone = scanner.nextLine();
                    System.out.print("Overall Rating: "); double rating = scanner.nextDouble();
                    scanner.nextLine();
                    
                    Document provider = new Document()
                        .append("name", name)
                        .append("email", email)
                        .append("phone", phone)
                        .append("overall_rating", rating);
                    
                    collection.insertOne(provider);
                    System.out.println("Provider added successfully!");
                    break;
                    
                case 2:
                    System.out.print("Enter Provider ID to delete: ");
                    String providerId = scanner.nextLine();
                    try {
                        Object id = parseId(providerId);
                        DeleteResult delResult = collection.deleteOne(Filters.eq("_id", id));
                        if (delResult.getDeletedCount() > 0) {
                            System.out.println("Provider deleted successfully!");
                        } else {
                            System.out.println("Provider not found with ID: " + providerId);
                        }
                    } catch (Exception e) {
                        System.out.println("Error deleting provider: " + e.getMessage());
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter Provider ID to modify: ");
                    String modProviderId = scanner.nextLine();
                    try {
                        Object id = parseId(modProviderId);
                        Document existing = collection.find(Filters.eq("_id", id)).first();
                        if (existing == null) {
                            System.out.println("Provider not found with ID: " + modProviderId);
                            break;
                        }
                        
                        System.out.println("Current Details:");
                        System.out.println("Name: " + existing.getString("name"));
                        System.out.println("Email: " + existing.getString("email"));
                        System.out.println("Phone: " + existing.getString("phone"));
                        System.out.println("Rating: " + existing.getDouble("overall_rating"));
                        System.out.println("\nEnter new details (press Enter to keep current value):");
                        
                        System.out.print("New Name: "); 
                        String newName = scanner.nextLine();
                        System.out.print("New Email: "); 
                        String newEmail = scanner.nextLine();
                        System.out.print("New Phone: "); 
                        String newPhone = scanner.nextLine();
                        System.out.print("New Rating: "); 
                        String ratingInput = scanner.nextLine();
                        
                        UpdateResult updateResult = collection.updateOne(
                            Filters.eq("_id", id),
                            Updates.combine(
                                Updates.set("name", newName.isEmpty() ? existing.getString("name") : newName),
                                Updates.set("email", newEmail.isEmpty() ? existing.getString("email") : newEmail),
                                Updates.set("phone", newPhone.isEmpty() ? existing.getString("phone") : newPhone),
                                Updates.set("overall_rating", ratingInput.isEmpty() ? 
                                    existing.getDouble("overall_rating") : Double.parseDouble(ratingInput))
                            )
                        );
                        if (updateResult.getModifiedCount() > 0) {
                            System.out.println("Provider modified successfully!");
                        } else {
                            System.out.println("No changes made to provider.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error modifying provider: " + e.getMessage());
                    }
                    break;
                    
                case 4:
                    System.out.println("\nAll Providers:");
                    int providerCount = 0;
                    for (Document doc : collection.find()) {
                        System.out.println("ID: " + doc.get("_id") + 
                                         ", Name: " + doc.getString("name") +
                                         ", Rating: " + doc.getDouble("overall_rating") +
                                         ", Phone: " + doc.getString("phone"));
                        providerCount++;
                    }
                    if (providerCount == 0) {
                        System.out.println("No providers found.");
                    }
                    break;
                    
                case 5:
                    return;
                    
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void manageServices() {
        while (true) {
            System.out.println("\n=== SERVICES MANAGEMENT ===");
            System.out.println("1. Add Service");
            System.out.println("2. Delete Service");
            System.out.println("3. Modify Service");
            System.out.println("4. View All Services");
            System.out.println("5. Back to Collection Menu");
            System.out.print("Choose: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            MongoCollection<Document> collection = database.getCollection("services");
            
            switch (choice) {
                case 1:
                    System.out.print("Name: "); String name = scanner.nextLine();
                    System.out.print("Category: "); String category = scanner.nextLine();
                    System.out.print("Base Price: "); double basePrice = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Description: "); String description = scanner.nextLine();
                    
                    Document service = new Document()
                        .append("name", name)
                        .append("category", category)
                        .append("base_price", basePrice)
                        .append("description", description);
                    
                    collection.insertOne(service);
                    System.out.println("Service added successfully!");
                    break;
                    
                case 2:
                    System.out.print("Enter Service ID to delete: ");
                    String serviceId = scanner.nextLine();
                    try {
                        Object id = parseId(serviceId);
                        DeleteResult delResult = collection.deleteOne(Filters.eq("_id", id));
                        if (delResult.getDeletedCount() > 0) {
                            System.out.println("Service deleted successfully!");
                        } else {
                            System.out.println("Service not found with ID: " + serviceId);
                        }
                    } catch (Exception e) {
                        System.out.println("Error deleting service: " + e.getMessage());
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter Service ID to modify: ");
                    String modServiceId = scanner.nextLine();
                    try {
                        Object id = parseId(modServiceId);
                        Document existing = collection.find(Filters.eq("_id", id)).first();
                        if (existing == null) {
                            System.out.println("Service not found with ID: " + modServiceId);
                            break;
                        }
                        
                        System.out.println("Current Details:");
                        System.out.println("Name: " + existing.getString("name"));
                        System.out.println("Category: " + existing.getString("category"));
                        System.out.println("Base Price: " + existing.getDouble("base_price"));
                        System.out.println("Description: " + existing.getString("description"));
                        System.out.println("\nEnter new details (press Enter to keep current value):");
                        
                        System.out.print("New Name: "); 
                        String newName = scanner.nextLine();
                        System.out.print("New Category: "); 
                        String newCategory = scanner.nextLine();
                        System.out.print("New Base Price: "); 
                        String priceInput = scanner.nextLine();
                        System.out.print("New Description: "); 
                        String newDescription = scanner.nextLine();
                        
                        UpdateResult updateResult = collection.updateOne(
                            Filters.eq("_id", id),
                            Updates.combine(
                                Updates.set("name", newName.isEmpty() ? existing.getString("name") : newName),
                                Updates.set("category", newCategory.isEmpty() ? existing.getString("category") : newCategory),
                                Updates.set("base_price", priceInput.isEmpty() ? 
                                    existing.getDouble("base_price") : Double.parseDouble(priceInput)),
                                Updates.set("description", newDescription.isEmpty() ? existing.getString("description") : newDescription)
                            )
                        );
                        if (updateResult.getModifiedCount() > 0) {
                            System.out.println("Service modified successfully!");
                        } else {
                            System.out.println("No changes made to service.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error modifying service: " + e.getMessage());
                    }
                    break;
                    
                case 4:
                    System.out.println("\nAll Services:");
                    int serviceCount = 0;
                    for (Document doc : collection.find()) {
                        System.out.println("ID: " + doc.get("_id") + 
                                         ", Name: " + doc.getString("name") +
                                         ", Category: " + doc.getString("category") +
                                         ", Price: $" + doc.getDouble("base_price"));
                        serviceCount++;
                    }
                    if (serviceCount == 0) {
                        System.out.println("No services found.");
                    }
                    break;
                    
                case 5:
                    return;
                    
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void manageBookings() {
        while (true) {
            System.out.println("\n=== BOOKINGS MANAGEMENT ===");
            System.out.println("1. Add Booking");
            System.out.println("2. Delete Booking");
            System.out.println("3. Modify Booking");
            System.out.println("4. View All Bookings");
            System.out.println("5. Back to Collection Menu");
            System.out.print("Choose: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            MongoCollection<Document> collection = database.getCollection("bookings");
            
            switch (choice) {
                case 1:
                    System.out.print("Customer ID: "); String custId = scanner.nextLine();
                    System.out.print("Service ID: "); String serviceId = scanner.nextLine();
                    System.out.print("Provider ID: "); String providerId = scanner.nextLine();
                    System.out.print("Booking Date (YYYY-MM-DD): "); String bookingDate = scanner.nextLine();
                    System.out.print("Service Date (YYYY-MM-DD): "); String serviceDate = scanner.nextLine();
                    System.out.print("Service Time (HH:MM): "); String serviceTime = scanner.nextLine();
                    System.out.print("Duration: "); String duration = scanner.nextLine();
                    System.out.print("Status: "); String status = scanner.nextLine();
                    System.out.print("Base Price: "); double basePrice = scanner.nextDouble();
                    System.out.print("GST: "); double gst = scanner.nextDouble();
                    System.out.print("Total Price: "); double totalPrice = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.print("Payment Method: "); String paymentMethod = scanner.nextLine();
                    
                    Document booking = new Document()
                        .append("customer_id", custId)
                        .append("service_id", serviceId)
                        .append("provider_id", providerId)
                        .append("booking_date", bookingDate)
                        .append("service_date", serviceDate)
                        .append("service_time", serviceTime)
                        .append("duration", duration)
                        .append("status", status)
                        .append("base_price", basePrice)
                        .append("gst", gst)
                        .append("total_price", totalPrice)
                        .append("payment_method", paymentMethod);
                    
                    collection.insertOne(booking);
                    System.out.println("Booking added successfully!");
                    break;
                    
                case 2:
                    System.out.print("Enter Booking ID to delete: ");
                    String bookingId = scanner.nextLine();
                    try {
                        Object id = parseId(bookingId);
                        DeleteResult delResult = collection.deleteOne(Filters.eq("_id", id));
                        if (delResult.getDeletedCount() > 0) {
                            System.out.println("Booking deleted successfully!");
                        } else {
                            System.out.println("Booking not found with ID: " + bookingId);
                        }
                    } catch (Exception e) {
                        System.out.println("Error deleting booking: " + e.getMessage());
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter Booking ID to modify: ");
                    String modBookingId = scanner.nextLine();
                    try {
                        Object id = parseId(modBookingId);
                        Document existing = collection.find(Filters.eq("_id", id)).first();
                        if (existing == null) {
                            System.out.println("Booking not found with ID: " + modBookingId);
                            break;
                        }
                        
                        System.out.println("Current Status: " + existing.getString("status"));
                        System.out.print("New Status: "); 
                        String newStatus = scanner.nextLine();
                        
                        if (newStatus.isEmpty()) {
                            System.out.println("No changes made.");
                            break;
                        }
                        
                        UpdateResult updateResult = collection.updateOne(
                            Filters.eq("_id", id),
                            Updates.set("status", newStatus)
                        );
                        if (updateResult.getModifiedCount() > 0) {
                            System.out.println("Booking status updated successfully!");
                        } else {
                            System.out.println("No changes made to booking.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error modifying booking: " + e.getMessage());
                    }
                    break;
                    
                case 4:
                    System.out.println("\nAll Bookings:");
                    int bookingCount = 0;
                    for (Document doc : collection.find()) {
                        System.out.println("ID: " + doc.get("_id") + 
                                         ", Customer: " + doc.getString("customer_id") +
                                         ", Service: " + doc.getString("service_id") +
                                         ", Status: " + doc.getString("status") +
                                         ", Total: $" + doc.getDouble("total_price"));
                        bookingCount++;
                    }
                    if (bookingCount == 0) {
                        System.out.println("No bookings found.");
                    }
                    break;
                    
                case 5:
                    return;
                    
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void manageProviderServices() {
        while (true) {
            System.out.println("\n=== PROVIDER SERVICES MANAGEMENT ===");
            System.out.println("1. Add Provider Service");
            System.out.println("2. Delete Provider Service");
            System.out.println("3. Modify Provider Service");
            System.out.println("4. View All Provider Services");
            System.out.println("5. Back to Collection Menu");
            System.out.print("Choose: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            MongoCollection<Document> collection = database.getCollection("provider_services");
            
            switch (choice) {
                case 1:
                    System.out.print("Provider ID: "); String providerId = scanner.nextLine();
                    System.out.print("Service ID: "); String serviceId = scanner.nextLine();
                    System.out.print("Experience Level: "); String experience = scanner.nextLine();
                    System.out.print("Price Modifier: "); double modifier = scanner.nextDouble();
                    scanner.nextLine();
                    
                    Document providerService = new Document()
                        .append("provider_id", providerId)
                        .append("service_id", serviceId)
                        .append("experience_level", experience)
                        .append("price_modifier", modifier);
                    
                    collection.insertOne(providerService);
                    System.out.println("Provider Service added successfully!");
                    break;
                    
                case 2:
                    System.out.print("Enter Provider ID: "); String delProviderId = scanner.nextLine();
                    System.out.print("Enter Service ID: "); String delServiceId = scanner.nextLine();
                    
                    try {
                        DeleteResult delResult = collection.deleteOne(
                            Filters.and(
                                Filters.eq("provider_id", delProviderId),
                                Filters.eq("service_id", delServiceId)
                            )
                        );
                        if (delResult.getDeletedCount() > 0) {
                            System.out.println("Provider Service deleted successfully!");
                        } else {
                            System.out.println("Provider Service not found with Provider ID: " + delProviderId + " and Service ID: " + delServiceId);
                        }
                    } catch (Exception e) {
                        System.out.println("Error deleting provider service: " + e.getMessage());
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter Provider ID: "); String modProviderId = scanner.nextLine();
                    System.out.print("Enter Service ID: "); String modServiceId = scanner.nextLine();
                    
                    try {
                        Document existing = collection.find(
                            Filters.and(
                                Filters.eq("provider_id", modProviderId),
                                Filters.eq("service_id", modServiceId)
                            )
                        ).first();
                        
                        if (existing == null) {
                            System.out.println("Provider Service not found.");
                            break;
                        }
                        
                        System.out.println("Current Details:");
                        System.out.println("Experience Level: " + existing.getString("experience_level"));
                        System.out.println("Price Modifier: " + existing.getDouble("price_modifier"));
                        System.out.println("\nEnter new details (press Enter to keep current value):");
                        
                        System.out.print("New Experience Level: "); 
                        String newExperience = scanner.nextLine();
                        System.out.print("New Price Modifier: "); 
                        String modifierInput = scanner.nextLine();
                        
                        UpdateResult updateResult = collection.updateOne(
                            Filters.and(
                                Filters.eq("provider_id", modProviderId),
                                Filters.eq("service_id", modServiceId)
                            ),
                            Updates.combine(
                                Updates.set("experience_level", newExperience.isEmpty() ? existing.getString("experience_level") : newExperience),
                                Updates.set("price_modifier", modifierInput.isEmpty() ? 
                                    existing.getDouble("price_modifier") : Double.parseDouble(modifierInput))
                            )
                        );
                        if (updateResult.getModifiedCount() > 0) {
                            System.out.println("Provider Service modified successfully!");
                        } else {
                            System.out.println("No changes made to provider service.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error modifying provider service: " + e.getMessage());
                    }
                    break;
                    
                case 4:
                    System.out.println("\nAll Provider Services:");
                    int psCount = 0;
                    for (Document doc : collection.find()) {
                        System.out.println("Provider: " + doc.getString("provider_id") + 
                                         ", Service: " + doc.getString("service_id") +
                                         ", Experience: " + doc.getString("experience_level") +
                                         ", Modifier: " + doc.getDouble("price_modifier"));
                        psCount++;
                    }
                    if (psCount == 0) {
                        System.out.println("No provider services found.");
                    }
                    break;
                    
                case 5:
                    return;
                    
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void manageFeedback() {
        while (true) {
            System.out.println("\n=== FEEDBACK MANAGEMENT ===");
            System.out.println("1. Add Feedback");
            System.out.println("2. Delete Feedback");
            System.out.println("3. Modify Feedback");
            System.out.println("4. View All Feedback");
            System.out.println("5. Back to Collection Menu");
            System.out.print("Choose: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            MongoCollection<Document> collection = database.getCollection("feedback");
            
            switch (choice) {
                case 1:
                    System.out.print("Booking ID: "); String bookingId = scanner.nextLine();
                    System.out.print("Rating (1-5): "); int rating = scanner.nextInt();
                    scanner.nextLine();
                    System.out.print("Comments: "); String comments = scanner.nextLine();
                    System.out.print("Feedback Date (YYYY-MM-DD): "); String feedbackDate = scanner.nextLine();
                    
                    Document feedback = new Document()
                        .append("booking_id", bookingId)
                        .append("rating", rating)
                        .append("comments", comments)
                        .append("feedback_date", feedbackDate);
                    
                    collection.insertOne(feedback);
                    System.out.println("Feedback added successfully!");
                    break;
                    
                case 2:
                    System.out.print("Enter Feedback ID to delete: ");
                    String feedbackId = scanner.nextLine();
                    try {
                        Object id = parseId(feedbackId);
                        DeleteResult delResult = collection.deleteOne(Filters.eq("_id", id));
                        if (delResult.getDeletedCount() > 0) {
                            System.out.println("Feedback deleted successfully!");
                        } else {
                            System.out.println("Feedback not found with ID: " + feedbackId);
                        }
                    } catch (Exception e) {
                        System.out.println("Error deleting feedback: " + e.getMessage());
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter Feedback ID to modify: ");
                    String modFeedbackId = scanner.nextLine();
                    try {
                        Object id = parseId(modFeedbackId);
                        Document existing = collection.find(Filters.eq("_id", id)).first();
                        if (existing == null) {
                            System.out.println("Feedback not found with ID: " + modFeedbackId);
                            break;
                        }
                        
                        System.out.println("Current Details:");
                        System.out.println("Rating: " + existing.getInteger("rating"));
                        System.out.println("Comments: " + existing.getString("comments"));
                        System.out.println("\nEnter new details (press Enter to keep current value):");
                        
                        System.out.print("New Rating (1-5): "); 
                        String ratingInput = scanner.nextLine();
                        System.out.print("New Comments: "); 
                        String newComments = scanner.nextLine();
                        
                        UpdateResult updateResult = collection.updateOne(
                            Filters.eq("_id", id),
                            Updates.combine(
                                Updates.set("rating", ratingInput.isEmpty() ? 
                                    existing.getInteger("rating") : Integer.parseInt(ratingInput)),
                                Updates.set("comments", newComments.isEmpty() ? existing.getString("comments") : newComments)
                            )
                        );
                        if (updateResult.getModifiedCount() > 0) {
                            System.out.println("Feedback modified successfully!");
                        } else {
                            System.out.println("No changes made to feedback.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error modifying feedback: " + e.getMessage());
                    }
                    break;
                    
                case 4:
                    System.out.println("\nAll Feedback:");
                    int feedbackCount = 0;
                    for (Document doc : collection.find()) {
                        System.out.println("ID: " + doc.get("_id") + 
                                         ", Booking: " + doc.getString("booking_id") +
                                         ", Rating: " + doc.getInteger("rating") +
                                         ", Comments: " + doc.getString("comments"));
                        feedbackCount++;
                    }
                    if (feedbackCount == 0) {
                        System.out.println("No feedback found.");
                    }
                    break;
                    
                case 5:
                    return;
                    
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    private static void manageBookingDetails() {
        while (true) {
            System.out.println("\n=== BOOKING DETAILS MANAGEMENT ===");
            System.out.println("1. Add Booking Details");
            System.out.println("2. Delete Booking Details");
            System.out.println("3. Modify Booking Details");
            System.out.println("4. View All Booking Details");
            System.out.println("5. Back to Collection Menu");
            System.out.print("Choose: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();
            
            MongoCollection<Document> collection = database.getCollection("booking_details");
            
            switch (choice) {
                case 1:
                    System.out.print("Booking ID: "); String bookingId = scanner.nextLine();
                    System.out.print("Special Instructions: "); String instructions = scanner.nextLine();
                    System.out.print("Service Specific Data (JSON): "); String jsonData = scanner.nextLine();
                    
                    try {
                        Document bookingDetails = new Document()
                            .append("booking_id", bookingId)
                            .append("special_instructions", instructions)
                            .append("service_specific_data", Document.parse(jsonData));
                        
                        collection.insertOne(bookingDetails);
                        System.out.println("Booking Details added successfully!");
                    } catch (Exception e) {
                        System.out.println("Error adding booking details. Make sure JSON is valid: " + e.getMessage());
                    }
                    break;
                    
                case 2:
                    System.out.print("Enter Booking Details ID to delete: ");
                    String detailsId = scanner.nextLine();
                    try {
                        Object id = parseId(detailsId);
                        DeleteResult delResult = collection.deleteOne(Filters.eq("_id", id));
                        if (delResult.getDeletedCount() > 0) {
                            System.out.println("Booking Details deleted successfully!");
                        } else {
                            System.out.println("Booking Details not found with ID: " + detailsId);
                        }
                    } catch (Exception e) {
                        System.out.println("Error deleting booking details: " + e.getMessage());
                    }
                    break;
                    
                case 3:
                    System.out.print("Enter Booking Details ID to modify: ");
                    String modDetailsId = scanner.nextLine();
                    try {
                        Object id = parseId(modDetailsId);
                        Document existing = collection.find(Filters.eq("_id", id)).first();
                        if (existing == null) {
                            System.out.println("Booking Details not found with ID: " + modDetailsId);
                            break;
                        }
                        
                        System.out.println("Current Instructions: " + existing.getString("special_instructions"));
                        System.out.print("New Special Instructions: "); 
                        String newInstructions = scanner.nextLine();
                        
                        if (newInstructions.isEmpty()) {
                            System.out.println("No changes made.");
                            break;
                        }
                        
                        UpdateResult updateResult = collection.updateOne(
                            Filters.eq("_id", id),
                            Updates.set("special_instructions", newInstructions)
                        );
                        if (updateResult.getModifiedCount() > 0) {
                            System.out.println("Booking Details modified successfully!");
                        } else {
                            System.out.println("No changes made to booking details.");
                        }
                    } catch (Exception e) {
                        System.out.println("Error modifying booking details: " + e.getMessage());
                    }
                    break;
                    
                case 4:
                    System.out.println("\nAll Booking Details:");
                    int detailsCount = 0;
                    for (Document doc : collection.find()) {
                        System.out.println("ID: " + doc.get("_id") + 
                                         ", Booking: " + doc.getString("booking_id") +
                                         ", Instructions: " + doc.getString("special_instructions"));
                        detailsCount++;
                    }
                    if (detailsCount == 0) {
                        System.out.println("No booking details found.");
                    }
                    break;
                    
                case 5:
                    return;
                    
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
    
    // Rest of your methods remain exactly the same (sample queries, subqueries, procedural programming)
    // Only the collection management methods above have been fixed
    
    private static void viewAllCollections() {
        System.out.println("\n=== ALL COLLECTIONS ===");
        
        String[] collections = {"customers", "providers", "services", "bookings", 
                               "provider_services", "feedback", "booking_details"};
        
        for (String collectionName : collections) {
            System.out.println("\n--- " + collectionName.toUpperCase() + " ---");
            MongoCollection<Document> collection = database.getCollection(collectionName);
            int count = 0;
            for (Document doc : collection.find()) {
                System.out.println(doc.toJson());
                count++;
                if (count >= 5) {
                    System.out.println("... and " + (collection.countDocuments() - 5) + " more");
                    break;
                }
            }
            if (count == 0) {
                System.out.println("No documents found.");
            }
        }
    }
    
    // Sample Queries (keep all your existing query methods exactly as they are)
    private static void sampleQueries() {
        System.out.println("\n=== SAMPLE QUERIES ===");
        
        // Query 1
        System.out.println("\n1. Find all customers from New York:");
        findCustomersByCity("New York");
        
        // Query 2
        System.out.println("\n2. Find providers with rating above 4.5:");
        findHighRatedProviders();
        
        // Query 3
        System.out.println("\n3. Find bookings for January 2024:");
        findBookingsByMonth("2024-01");
        
        // Query 4
        System.out.println("\n4. Count total services in each category:");
        countServicesByCategory();
        
        // Query 5
        System.out.println("\n5. Find completed bookings:");
        findCompletedBookings();
        
        // Query 6
        System.out.println("\n6. Find services with base price less than $100:");
        findAffordableServices();
        
        // Query 7
        System.out.println("\n7. Find providers offering Plumbing services:");
        findProvidersByService("Plumbing Repair");
        
        // Query 8
        System.out.println("\n8. Calculate total revenue by payment method:");
        calculateRevenueByPaymentMethod();
        
        // Query 9
        System.out.println("\n9. Find customers with their booking counts:");
        findCustomersWithBookingCounts();
        
        // Query 10
        System.out.println("\n10. Find average service price by category:");
        findAveragePriceByCategory();
    }
    
    // Sample Subqueries (keep all your existing subquery methods exactly as they are)
    private static void sampleSubqueries() {
        System.out.println("\n=== SAMPLE SUBQUERIES ===");
        
        // Subquery 1
        System.out.println("\n1. Find customers who booked services above average price:");
        findCustomersWithExpensiveBookings();
        
        // Subquery 2
        System.out.println("\n2. Find providers with above average ratings:");
        findProvidersAboveAverageRating();
        
        // Subquery 3
        System.out.println("\n3. Find services that have no bookings:");
        findServicesWithoutBookings();
        
        // Subquery 4
        System.out.println("\n4. Find customers with highest total spending:");
        findTopSpendingCustomers();
        
        // Subquery 5
        System.out.println("\n5. Find providers with most completed bookings:");
        findMostBusyProviders();
        
        // Subquery 6
        System.out.println("\n6. Find services with highest average rating:");
        findBestRatedServices();
        
        // Subquery 7
        System.out.println("\n7. Find customers who never gave feedback:");
        findCustomersWithoutFeedback();
        
        // Subquery 8
        System.out.println("\n8. Find monthly booking trends:");
        findMonthlyBookingTrends();
        
        // Subquery 9
        System.out.println("\n9. Find providers with expert level experience:");
        findExpertProviders();
        
        // Subquery 10
        System.out.println("\n10. Find customers with multiple service categories:");
        findCustomersWithMultipleCategories();
    }
    
    // Procedural Programming Examples (keep all your existing procedural methods exactly as they are)
    private static void proceduralProgramming() {
        System.out.println("\n=== FUNCTIONS PROGRAMMING EXAMPLES ===");
        
        // Example 1
        System.out.println("\n1. Calculate and update provider overall ratings:");
        updateProviderRatings();
        
        // Example 2
        System.out.println("\n2. Generate monthly revenue report:");
        generateMonthlyReport();
        
        // Example 3
        System.out.println("\n3. Automated booking status update:");
        updateBookingStatuses();
        
        // Example 4
        System.out.println("\n4. Customer loyalty program calculation:");
        calculateLoyaltyPoints();
        
        // Example 5
        System.out.println("\n5. Service price adjustment based on demand:");
        adjustServicePrices();
        
        // Example 6
        System.out.println("\n6. Provider performance analysis:");
        analyzeProviderPerformance();
        
        // Example 7
        System.out.println("\n7. Customer segmentation based on spending:");
        segmentCustomers();
        
        // Example 8
        System.out.println("\n8. Booking conflict detection:");
        detectBookingConflicts();
        
        // Example 9
        System.out.println("\n9. Revenue forecasting:");
        forecastRevenue();
        
        // Example 10
        System.out.println("\n10. Automated feedback analysis:");
        analyzeFeedbackSentiment();
    }
    
    // All your existing implementation methods for queries, subqueries, and procedural programming
    // Keep them exactly as they were in your original code...
    // [Include all your existing findCustomersByCity, findHighRatedProviders, etc. methods here]
    // They should remain unchanged from your previous version
    
    // Implementation of query methods
    private static void findCustomersByCity(String city) {
        MongoCollection<Document> collection = database.getCollection("customers");
        var results = collection.find(Filters.regex("address", city));
        int count = 0;
        for (Document doc : results) {
            System.out.println("Customer: " + doc.getString("name") + 
                             ", Address: " + doc.getString("address"));
            count++;
        }
        if (count == 0) {
            System.out.println("No customers found in " + city);
        }
    }
    
    private static void findHighRatedProviders() {
        MongoCollection<Document> collection = database.getCollection("providers");
        var results = collection.find(Filters.gt("overall_rating", 4.5));
        int count = 0;
        for (Document doc : results) {
            System.out.println("Provider: " + doc.getString("name") + 
                             ", Rating: " + doc.getDouble("overall_rating"));
            count++;
        }
        if (count == 0) {
            System.out.println("No providers found with rating above 4.5");
        }
    }
        
    private static void findBookingsByMonth(String month) {
        MongoCollection<Document> collection = database.getCollection("bookings");
        var results = collection.find(Filters.regex("booking_date", "^" + month));
        int count = 0;
        for (Document doc : results) {
            System.out.println("Booking ID: " + doc.get("_id") + 
                             ", Date: " + doc.getString("booking_date") +
                             ", Customer: " + doc.getString("customer_id") +
                             ", Total: $" + doc.getDouble("total_price"));
            count++;
        }
        if (count == 0) {
            System.out.println("No bookings found for " + month);
        }
    }
    
    private static void countServicesByCategory() {
        MongoCollection<Document> collection = database.getCollection("services");
        var results = collection.find();
        var categoryCount = new java.util.HashMap<String, Integer>();
        
        for (Document doc : results) {
            String category = doc.getString("category");
            categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
        }
        
        for (var entry : categoryCount.entrySet()) {
            System.out.println("Category: " + entry.getKey() + ", Count: " + entry.getValue());
        }
    }
    
    private static void findCompletedBookings() {
        MongoCollection<Document> collection = database.getCollection("bookings");
        var results = collection.find(Filters.eq("status", "Completed"));
        int count = 0;
        double totalRevenue = 0;
        for (Document doc : results) {
            System.out.println("Booking: " + doc.get("_id") + 
                             ", Customer: " + doc.getString("customer_id") +
                             ", Total: $" + doc.getDouble("total_price"));
            totalRevenue += doc.getDouble("total_price");
            count++;
        }
        System.out.println("Total completed bookings: " + count);
        System.out.println("Total revenue from completed bookings: $" + totalRevenue);
    }
    
    private static void findAffordableServices() {
        MongoCollection<Document> collection = database.getCollection("services");
        var results = collection.find(Filters.lt("base_price", 100.0));
        int count = 0;
        for (Document doc : results) {
            System.out.println("Service: " + doc.getString("name") + 
                             ", Category: " + doc.getString("category") +
                             ", Price: $" + doc.getDouble("base_price"));
            count++;
        }
        if (count == 0) {
            System.out.println("No services found under $100");
        }
    }
    
    private static void findProvidersByService(String serviceName) {
        MongoCollection<Document> services = database.getCollection("services");
        MongoCollection<Document> providerServices = database.getCollection("provider_services");
        MongoCollection<Document> providers = database.getCollection("providers");
        
        // Find service by name
        Document service = services.find(Filters.eq("name", serviceName)).first();
        if (service == null) {
            System.out.println("Service not found: " + serviceName);
            return;
        }
        
        String serviceId = service.get("_id").toString();
        var psResults = providerServices.find(Filters.eq("service_id", serviceId));
        
        int count = 0;
        for (Document ps : psResults) {
            String providerId = ps.getString("provider_id");
            Document provider = providers.find(Filters.eq("_id", providerId)).first();
            if (provider != null) {
                System.out.println("Provider: " + provider.getString("name") +
                                 ", Experience: " + ps.getString("experience_level") +
                                 ", Price Modifier: " + ps.getDouble("price_modifier"));
                count++;
            }
        }
        
        if (count == 0) {
            System.out.println("No providers found for service: " + serviceName);
        }
    }
    
    private static void calculateRevenueByPaymentMethod() {
        MongoCollection<Document> collection = database.getCollection("bookings");
        var results = collection.find();
        var revenueByMethod = new java.util.HashMap<String, Double>();
        
        for (Document doc : results) {
            String method = doc.getString("payment_method");
            double total = doc.getDouble("total_price");
            revenueByMethod.put(method, revenueByMethod.getOrDefault(method, 0.0) + total);
        }
        
        for (var entry : revenueByMethod.entrySet()) {
            System.out.println("Payment Method: " + entry.getKey() + ", Total Revenue: $" + entry.getValue());
        }
    }
    
    private static void findCustomersWithBookingCounts() {
        MongoCollection<Document> bookings = database.getCollection("bookings");
        MongoCollection<Document> customers = database.getCollection("customers");
        
        var bookingCounts = new java.util.HashMap<String, Integer>();
        
        for (Document booking : bookings.find()) {
            String customerId = booking.getString("customer_id");
            bookingCounts.put(customerId, bookingCounts.getOrDefault(customerId, 0) + 1);
        }
        
        for (var entry : bookingCounts.entrySet()) {
            Document customer = customers.find(Filters.eq("_id", entry.getKey())).first();
            if (customer != null) {
                System.out.println("Customer: " + customer.getString("name") + 
                                 ", Booking Count: " + entry.getValue());
            }
        }
    }
    
    private static void findAveragePriceByCategory() {
        MongoCollection<Document> collection = database.getCollection("services");
        var results = collection.find();
        var categoryStats = new java.util.HashMap<String, java.util.List<Double>>();
        
        for (Document doc : results) {
            String category = doc.getString("category");
            double price = doc.getDouble("base_price");
            categoryStats.putIfAbsent(category, new java.util.ArrayList<>());
            categoryStats.get(category).add(price);
        }
        
        for (var entry : categoryStats.entrySet()) {
            double avg = entry.getValue().stream().mapToDouble(Double::doubleValue).average().orElse(0);
            System.out.println("Category: " + entry.getKey() + ", Average Price: $" + String.format("%.2f", avg));
        }
    }
    
    // Implementation of subquery methods
    private static void findCustomersWithExpensiveBookings() {
        MongoCollection<Document> bookings = database.getCollection("bookings");
        MongoCollection<Document> customers = database.getCollection("customers");
        
        // Calculate average booking price
        double avgPrice = calculateAverageBookingPrice();
        
        System.out.println("Average booking price: $" + String.format("%.2f", avgPrice));
        
        // Find customers with bookings above average
        var expensiveBookings = bookings.find(Filters.gt("total_price", avgPrice));
        var customerIds = new java.util.HashSet<String>();
        
        for (Document booking : expensiveBookings) {
            customerIds.add(booking.getString("customer_id"));
        }
        
        System.out.println("Customers with expensive bookings:");
        for (String custId : customerIds) {
            Document customer = customers.find(Filters.eq("_id", custId)).first();
            if (customer != null) {
                System.out.println("Customer: " + customer.getString("name"));
            }
        }
    }
    
    private static double calculateAverageBookingPrice() {
        MongoCollection<Document> collection = database.getCollection("bookings");
        double total = 0;
        int count = 0;
        
        for (Document doc : collection.find()) {
            total += doc.getDouble("total_price");
            count++;
        }
        
        return count > 0 ? total / count : 0;
    }
    
    private static void findProvidersAboveAverageRating() {
        MongoCollection<Document> providers = database.getCollection("providers");
        
        // Calculate average rating
        double totalRating = 0;
        int count = 0;
        for (Document provider : providers.find()) {
            totalRating += provider.getDouble("overall_rating");
            count++;
        }
        double avgRating = count > 0 ? totalRating / count : 0;
        
        System.out.println("Average provider rating: " + String.format("%.2f", avgRating));
        
        // Find providers above average
        var results = providers.find(Filters.gt("overall_rating", avgRating));
        System.out.println("Providers above average rating:");
        for (Document provider : results) {
            System.out.println("Provider: " + provider.getString("name") + 
                             ", Rating: " + provider.getDouble("overall_rating"));
        }
    }
    
    private static void findServicesWithoutBookings() {
        MongoCollection<Document> services = database.getCollection("services");
        MongoCollection<Document> bookings = database.getCollection("bookings");
        
        var bookedServiceIds = new java.util.HashSet<String>();
        for (Document booking : bookings.find()) {
            bookedServiceIds.add(booking.getString("service_id"));
        }
        
        System.out.println("Services without bookings:");
        for (Document service : services.find()) {
            String serviceId = service.get("_id").toString();
            if (!bookedServiceIds.contains(serviceId)) {
                System.out.println("Service: " + service.getString("name") + 
                                 ", Category: " + service.getString("category"));
            }
        }
    }
    
    private static void findTopSpendingCustomers() {
        MongoCollection<Document> bookings = database.getCollection("bookings");
        MongoCollection<Document> customers = database.getCollection("customers");
        
        var customerSpending = new java.util.HashMap<String, Double>();
        
        for (Document booking : bookings.find()) {
            String customerId = booking.getString("customer_id");
            double total = booking.getDouble("total_price");
            customerSpending.put(customerId, customerSpending.getOrDefault(customerId, 0.0) + total);
        }
        
        System.out.println("Top spending customers:");
        customerSpending.entrySet().stream()
            .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
            .limit(5)
            .forEach(entry -> {
                Document customer = customers.find(Filters.eq("_id", entry.getKey())).first();
                if (customer != null) {
                    System.out.println("Customer: " + customer.getString("name") + 
                                     ", Total Spending: $" + String.format("%.2f", entry.getValue()));
                }
            });
    }
    
    private static void findMostBusyProviders() {
        MongoCollection<Document> bookings = database.getCollection("bookings");
        MongoCollection<Document> providers = database.getCollection("providers");
        
        var providerBookingCount = new java.util.HashMap<String, Integer>();
        
        for (Document booking : bookings.find(Filters.eq("status", "Completed"))) {
            String providerId = booking.getString("provider_id");
            providerBookingCount.put(providerId, providerBookingCount.getOrDefault(providerId, 0) + 1);
        }
        
        System.out.println("Most busy providers (completed bookings):");
        providerBookingCount.entrySet().stream()
            .sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
            .limit(5)
            .forEach(entry -> {
                Document provider = providers.find(Filters.eq("_id", entry.getKey())).first();
                if (provider != null) {
                    System.out.println("Provider: " + provider.getString("name") + 
                                     ", Completed Bookings: " + entry.getValue());
                }
            });
    }
    
    private static void findBestRatedServices() {
        MongoCollection<Document> feedback = database.getCollection("feedback");
        MongoCollection<Document> bookings = database.getCollection("bookings");
        MongoCollection<Document> services = database.getCollection("services");
        
        var serviceRatings = new java.util.HashMap<String, java.util.List<Integer>>();
        
        for (Document fb : feedback.find()) {
            String bookingId = fb.getString("booking_id");
            Document booking = bookings.find(Filters.eq("_id", bookingId)).first();
            if (booking != null) {
                String serviceId = booking.getString("service_id");
                serviceRatings.putIfAbsent(serviceId, new java.util.ArrayList<>());
                serviceRatings.get(serviceId).add(fb.getInteger("rating"));
            }
        }
        
        System.out.println("Best rated services:");
        serviceRatings.entrySet().stream()
            .map(entry -> {
                String serviceId = entry.getKey();
                double avgRating = entry.getValue().stream().mapToInt(Integer::intValue).average().orElse(0);
                Document service = services.find(Filters.eq("_id", serviceId)).first();
                return new ServiceRating(service != null ? service.getString("name") : "Unknown", avgRating);
            })
            .sorted((a, b) -> Double.compare(b.rating, a.rating))
            .limit(5)
            .forEach(sr -> System.out.println("Service: " + sr.name + ", Average Rating: " + String.format("%.2f", sr.rating)));
    }
    
    private static class ServiceRating {
        String name;
        double rating;
        
        ServiceRating(String name, double rating) {
            this.name = name;
            this.rating = rating;
        }
    }
    
    private static void findCustomersWithoutFeedback() {
        MongoCollection<Document> bookings = database.getCollection("bookings");
        MongoCollection<Document> feedback = database.getCollection("feedback");
        MongoCollection<Document> customers = database.getCollection("customers");
        
        var feedbackBookingIds = new java.util.HashSet<String>();
        for (Document fb : feedback.find()) {
            feedbackBookingIds.add(fb.getString("booking_id"));
        }
        
        var customersWithoutFeedback = new java.util.HashSet<String>();
        for (Document booking : bookings.find()) {
            if (!feedbackBookingIds.contains(booking.get("_id").toString())) {
                customersWithoutFeedback.add(booking.getString("customer_id"));
            }
        }
        
        System.out.println("Customers who never gave feedback:");
        for (String custId : customersWithoutFeedback) {
            Document customer = customers.find(Filters.eq("_id", custId)).first();
            if (customer != null) {
                System.out.println("Customer: " + customer.getString("name"));
            }
        }
    }
    
    private static void findMonthlyBookingTrends() {
        MongoCollection<Document> bookings = database.getCollection("bookings");
        
        var monthlyCounts = new java.util.HashMap<String, Integer>();
        
        for (Document booking : bookings.find()) {
            String bookingDate = booking.getString("booking_date");
            if (bookingDate != null && bookingDate.length() >= 7) {
                String month = bookingDate.substring(0, 7); // YYYY-MM
                monthlyCounts.put(month, monthlyCounts.getOrDefault(month, 0) + 1);
            }
        }
        
        System.out.println("Monthly booking trends:");
        monthlyCounts.entrySet().stream()
            .sorted(java.util.Map.Entry.comparingByKey())
            .forEach(entry -> System.out.println("Month: " + entry.getKey() + ", Bookings: " + entry.getValue()));
    }
    
    private static void findExpertProviders() {
        MongoCollection<Document> providerServices = database.getCollection("provider_services");
        MongoCollection<Document> providers = database.getCollection("providers");
        
        var results = providerServices.find(Filters.eq("experience_level", "Expert"));
        
        System.out.println("Providers with expert level experience:");
        for (Document ps : results) {
            String providerId = ps.getString("provider_id");
            Document provider = providers.find(Filters.eq("_id", providerId)).first();
            if (provider != null) {
                System.out.println("Provider: " + provider.getString("name") +
                                 ", Service: " + ps.getString("service_id") +
                                 ", Experience: Expert");
            }
        }
    }
    
    private static void findCustomersWithMultipleCategories() {
        // This is a complex query that would typically use aggregation
        // Simplified implementation for demonstration
        System.out.println("Customers who booked services from multiple categories:");
        System.out.println("(This would require complex aggregation in a real scenario)");
    }
    
    // Implementation of procedural programming methods
    private static void updateProviderRatings() {
        MongoCollection<Document> providers = database.getCollection("providers");
        MongoCollection<Document> feedback = database.getCollection("feedback");
        MongoCollection<Document> bookings = database.getCollection("bookings");
        
        var allProviders = providers.find();
        int updatedCount = 0;
        
        for (Document provider : allProviders) {
            String providerId = provider.get("_id").toString();
            
            // Find all bookings for this provider
            var providerBookings = bookings.find(Filters.eq("provider_id", providerId));
            var bookingIds = new ArrayList<String>();
            
            for (Document booking : providerBookings) {
                bookingIds.add(booking.get("_id").toString());
            }
            
            // Calculate average rating from feedback
            double totalRating = 0;
            int ratingCount = 0;
            
            for (String bookingId : bookingIds) {
                Document fb = feedback.find(Filters.eq("booking_id", bookingId)).first();
                if (fb != null) {
                    totalRating += fb.getInteger("rating");
                    ratingCount++;
                }
            }
            
            if (ratingCount > 0) {
                double newRating = totalRating / ratingCount;
                providers.updateOne(
                    Filters.eq("_id", providerId),
                    Updates.set("overall_rating", Math.round(newRating * 100.0) / 100.0)
                );
                System.out.println("Updated provider " + provider.getString("name") + 
                                 " with new rating: " + String.format("%.2f", newRating));
                updatedCount++;
            }
        }
        
        System.out.println("Updated ratings for " + updatedCount + " providers.");
    }
    
    private static void generateMonthlyReport() {
        MongoCollection<Document> bookings = database.getCollection("bookings");
        
        var monthlyStats = new java.util.HashMap<String, MonthlyStat>();
        
        for (Document booking : bookings.find()) {
            String bookingDate = booking.getString("booking_date");
            if (bookingDate != null && bookingDate.length() >= 7) {
                String month = bookingDate.substring(0, 7);
                MonthlyStat stat = monthlyStats.getOrDefault(month, new MonthlyStat());
                stat.totalRevenue += booking.getDouble("total_price");
                stat.bookingCount++;
                monthlyStats.put(month, stat);
            }
        }
        
        System.out.println("Monthly Revenue Report:");
        System.out.println("Month\t\tBookings\tTotal Revenue\tAverage Revenue");
        System.out.println("------------------------------------------------------------");
        monthlyStats.entrySet().stream()
            .sorted(java.util.Map.Entry.comparingByKey())
            .forEach(entry -> {
                String month = entry.getKey();
                MonthlyStat stat = entry.getValue();
                double avgRevenue = stat.bookingCount > 0 ? stat.totalRevenue / stat.bookingCount : 0;
                System.out.println(month + "\t" + stat.bookingCount + "\t\t$" + 
                                 String.format("%.2f", stat.totalRevenue) + "\t$" + 
                                 String.format("%.2f", avgRevenue));
            });
    }
    
    private static class MonthlyStat {
        int bookingCount = 0;
        double totalRevenue = 0;
    }
    
    private static void updateBookingStatuses() {
        MongoCollection<Document> bookings = database.getCollection("bookings");
        
        // Update pending bookings older than today to "Expired"
        String today = java.time.LocalDate.now().toString();
        UpdateResult result = bookings.updateMany(
            Filters.and(
                Filters.eq("status", "Pending"),
                Filters.lt("service_date", today)
            ),
            Updates.set("status", "Expired")
        );
        
        System.out.println("Updated " + result.getModifiedCount() + " bookings from Pending to Expired.");
        
        // Update confirmed bookings for today to "In Progress"
        result = bookings.updateMany(
            Filters.and(
                Filters.eq("status", "Confirmed"),
                Filters.eq("service_date", today)
            ),
            Updates.set("status", "In Progress")
        );
        
        System.out.println("Updated " + result.getModifiedCount() + " bookings from Confirmed to In Progress.");
    }
    
    private static void calculateLoyaltyPoints() {
        MongoCollection<Document> bookings = database.getCollection("bookings");
        MongoCollection<Document> customers = database.getCollection("customers");
        
        var customerPoints = new java.util.HashMap<String, Integer>();
        
        for (Document booking : bookings.find(Filters.eq("status", "Completed"))) {
            String customerId = booking.getString("customer_id");
            double total = booking.getDouble("total_price");
            int points = (int) (total / 10); // 1 point for every $10 spent
            customerPoints.put(customerId, customerPoints.getOrDefault(customerId, 0) + points);
        }
        
        System.out.println("Customer Loyalty Points:");
        for (var entry : customerPoints.entrySet()) {
            Document customer = customers.find(Filters.eq("_id", entry.getKey())).first();
            if (customer != null) {
                System.out.println("Customer: " + customer.getString("name") + 
                                 ", Loyalty Points: " + entry.getValue());
            }
        }
    }
    
    private static void adjustServicePrices() {
        MongoCollection<Document> services = database.getCollection("services");
        MongoCollection<Document> bookings = database.getCollection("bookings");
        
        var serviceBookingCount = new java.util.HashMap<String, Integer>();
        
        for (Document booking : bookings.find()) {
            String serviceId = booking.getString("service_id");
            serviceBookingCount.put(serviceId, serviceBookingCount.getOrDefault(serviceId, 0) + 1);
        }
        
        System.out.println("Service Price Adjustments based on demand:");
        for (Document service : services.find()) {
            String serviceId = service.get("_id").toString();
            int bookingsCount = serviceBookingCount.getOrDefault(serviceId, 0);
            double currentPrice = service.getDouble("base_price");
            double newPrice = currentPrice;
            
            if (bookingsCount > 5) {
                newPrice = currentPrice * 1.1; // Increase price by 10% for high demand
                System.out.println("Service: " + service.getString("name") + 
                                 " - High demand, price increased from $" + 
                                 String.format("%.2f", currentPrice) + " to $" + 
                                 String.format("%.2f", newPrice));
            } else if (bookingsCount == 0) {
                newPrice = currentPrice * 0.9; // Decrease price by 10% for no demand
                System.out.println("Service: " + service.getString("name") + 
                                 " - Low demand, price decreased from $" + 
                                 String.format("%.2f", currentPrice) + " to $" + 
                                 String.format("%.2f", newPrice));
            }
            
            // Update price in database
            if (newPrice != currentPrice) {
                services.updateOne(
                    Filters.eq("_id", serviceId),
                    Updates.set("base_price", Math.round(newPrice * 100.0) / 100.0)
                );
            }
        }
    }
    
    private static void analyzeProviderPerformance() {
        MongoCollection<Document> providers = database.getCollection("providers");
        MongoCollection<Document> bookings = database.getCollection("bookings");
        MongoCollection<Document> feedback = database.getCollection("feedback");
        
        System.out.println("Provider Performance Analysis:");
        System.out.println("Provider\t\tRating\tCompleted\tRevenue");
        System.out.println("------------------------------------------------");
        
        for (Document provider : providers.find()) {
            String providerId = provider.get("_id").toString();
            
            // Count completed bookings
            long completedBookings = bookings.countDocuments(
                Filters.and(
                    Filters.eq("provider_id", providerId),
                    Filters.eq("status", "Completed")
                )
            );
            
            // Calculate total revenue
            double totalRevenue = 0;
            for (Document booking : bookings.find(
                Filters.and(
                    Filters.eq("provider_id", providerId),
                    Filters.eq("status", "Completed")
                )
            )) {
                totalRevenue += booking.getDouble("total_price");
            }
            
            System.out.println(provider.getString("name") + "\t" + 
                             provider.getDouble("overall_rating") + "\t" + 
                             completedBookings + "\t\t$" + 
                             String.format("%.2f", totalRevenue));
        }
    }
    
    private static void segmentCustomers() {
        MongoCollection<Document> customers = database.getCollection("customers");
        MongoCollection<Document> bookings = database.getCollection("bookings");
        
        var customerSegments = new java.util.HashMap<String, java.util.List<String>>();
        customerSegments.put("High Value", new java.util.ArrayList<>());
        customerSegments.put("Medium Value", new java.util.ArrayList<>());
        customerSegments.put("Low Value", new java.util.ArrayList<>());
        
        for (Document customer : customers.find()) {
            String customerId = customer.get("_id").toString();
            double totalSpending = 0;
            int bookingCount = 0;
            
            for (Document booking : bookings.find(Filters.eq("customer_id", customerId))) {
                totalSpending += booking.getDouble("total_price");
                bookingCount++;
            }
            
            String segment;
            if (totalSpending > 500) {
                segment = "High Value";
            } else if (totalSpending > 100) {
                segment = "Medium Value";
            } else {
                segment = "Low Value";
            }
            
            customerSegments.get(segment).add(customer.getString("name") + 
                                            " (Spent: $" + String.format("%.2f", totalSpending) + 
                                            ", Bookings: " + bookingCount + ")");
        }
        
        System.out.println("Customer Segmentation:");
        for (var entry : customerSegments.entrySet()) {
            System.out.println("\n" + entry.getKey() + " Customers:");
            for (String customerInfo : entry.getValue()) {
                System.out.println("  - " + customerInfo);
            }
        }
    }
    
    private static void detectBookingConflicts() {
        MongoCollection<Document> bookings = database.getCollection("bookings");
        
        System.out.println("Checking for booking conflicts...");
        
        // Group bookings by provider and service date
        var providerDateBookings = new java.util.HashMap<String, java.util.List<Document>>();
        
        for (Document booking : bookings.find()) {
            String key = booking.getString("provider_id") + "_" + booking.getString("service_date");
            providerDateBookings.putIfAbsent(key, new java.util.ArrayList<>());
            providerDateBookings.get(key).add(booking);
        }
        
        // Check for time conflicts
        int conflictCount = 0;
        for (var entry : providerDateBookings.entrySet()) {
            var bookingsList = entry.getValue();
            if (bookingsList.size() > 1) {
                // Simple conflict detection - in real scenario, check time overlaps
                System.out.println("Potential conflict for provider on " + 
                                 bookingsList.get(0).getString("service_date") + 
                                 " with " + bookingsList.size() + " bookings");
                conflictCount++;
            }
        }
        
        System.out.println("Found " + conflictCount + " potential booking conflicts.");
    }
    
    private static void forecastRevenue() {
        MongoCollection<Document> bookings = database.getCollection("bookings");
        
        var monthlyRevenue = new java.util.HashMap<String, Double>();
        
        for (Document booking : bookings.find()) {
            String bookingDate = booking.getString("booking_date");
            if (bookingDate != null && bookingDate.length() >= 7) {
                String month = bookingDate.substring(0, 7);
                double revenue = booking.getDouble("total_price");
                monthlyRevenue.put(month, monthlyRevenue.getOrDefault(month, 0.0) + revenue);
            }
        }
        
        System.out.println("Revenue Forecasting (simple moving average):");
        
        // Simple forecasting using average of last 3 months
        var sortedMonths = monthlyRevenue.entrySet().stream()
            .sorted(java.util.Map.Entry.comparingByKey())
            .toList();
        
        if (sortedMonths.size() >= 3) {
            double last3MonthsAvg = sortedMonths.subList(sortedMonths.size() - 3, sortedMonths.size())
                .stream()
                .mapToDouble(entry -> entry.getValue())
                .average()
                .orElse(0);
            
            System.out.println("Average revenue for last 3 months: $" + String.format("%.2f", last3MonthsAvg));
            System.out.println("Next month forecast: $" + String.format("%.2f", last3MonthsAvg));
        }
    }
    
    private static void analyzeFeedbackSentiment() {
        MongoCollection<Document> feedback = database.getCollection("feedback");
        
        System.out.println("Feedback Sentiment Analysis:");
        
        var ratingDistribution = new java.util.HashMap<Integer, Integer>();
        var positiveWords = Arrays.asList("excellent", "great", "good", "perfect", "recommended");
        var negativeWords = Arrays.asList("bad", "poor", "terrible", "awful", "late");
        
        int positiveCount = 0;
        int negativeCount = 0;
        int neutralCount = 0;
        
        for (Document fb : feedback.find()) {
            int rating = fb.getInteger("rating");
            String comments = fb.getString("comments").toLowerCase();
            
            // Count rating distribution
            ratingDistribution.put(rating, ratingDistribution.getOrDefault(rating, 0) + 1);
            
            // Simple sentiment analysis based on keywords
            boolean hasPositive = positiveWords.stream().anyMatch(comments::contains);
            boolean hasNegative = negativeWords.stream().anyMatch(comments::contains);
            
            if (hasPositive && !hasNegative) {
                positiveCount++;
            } else if (hasNegative && !hasPositive) {
                negativeCount++;
            } else {
                neutralCount++;
            }
        }
        
        System.out.println("Rating Distribution:");
        for (int i = 1; i <= 5; i++) {
            System.out.println(i + " stars: " + ratingDistribution.getOrDefault(i, 0) + " feedbacks");
        }
        
        System.out.println("\nSentiment Analysis:");
        System.out.println("Positive: " + positiveCount);
        System.out.println("Negative: " + negativeCount);
        System.out.println("Neutral: " + neutralCount);
        
        int total = positiveCount + negativeCount + neutralCount;
        if (total > 0) {
            System.out.println("Satisfaction Rate: " + 
                             String.format("%.1f", (positiveCount * 100.0 / total)) + "%");
        }
    }
}