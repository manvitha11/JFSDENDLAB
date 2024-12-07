package com.klef.jfsd.exam;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.*;
import java.util.List;

public class ClientDemo {
    public static void main(String[] args) {
        // Configure Hibernate
        Configuration cfg = new Configuration();
        cfg.configure("hibernate.cfg.xml");
        SessionFactory factory = cfg.buildSessionFactory();

        // Insert records
        insertRecords(factory);

        // Apply criteria queries
        performCriteriaQueries(factory);

        factory.close();
    }

    /**
     * Inserts sample records into the database.
     */
    private static void insertRecords(SessionFactory factory) {
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        // Creating sample customer records
        Customer customer1 = new Customer();
        customer1.setName("Alice");
        customer1.setEmail("alice@example.com");
        customer1.setAge(25);
        customer1.setLocation("New York");

        Customer customer2 = new Customer();
        customer2.setName("Bob");
        customer2.setEmail("bob@example.com");
        customer2.setAge(30);
        customer2.setLocation("Los Angeles");

        Customer customer3 = new Customer();
        customer3.setName("Charlie");
        customer3.setEmail("charlie@example.com");
        customer3.setAge(35);
        customer3.setLocation("Chicago");

        // Persisting customer objects
        session.save(customer1);
        session.save(customer2);
        session.save(customer3);

        tx.commit(); // Committing the transaction
        session.close(); // Closing the session

        System.out.println("Records inserted successfully!");
    }

    /**
     * Demonstrates JPA Criteria API queries with restrictions.
     */
    private static void performCriteriaQueries(SessionFactory factory) {
        Session session = factory.openSession();

        System.out.println("\nApplying Criteria Queries:");

        // Create CriteriaBuilder
        CriteriaBuilder builder = session.getCriteriaBuilder();

        // 1. Equal Restriction
        System.out.println("Customers in New York:");
        CriteriaQuery<Customer> eqQuery = builder.createQuery(Customer.class);
        Root<Customer> rootEq = eqQuery.from(Customer.class);
        eqQuery.select(rootEq).where(builder.equal(rootEq.get("location"), "New York"));
        List<Customer> eqResult = session.createQuery(eqQuery).getResultList();
        eqResult.forEach(c -> System.out.println(c.getName()));

        // 2. Greater Than Restriction
        System.out.println("\nCustomers aged greater than 30:");
        CriteriaQuery<Customer> gtQuery = builder.createQuery(Customer.class);
        Root<Customer> rootGt = gtQuery.from(Customer.class);
        gtQuery.select(rootGt).where(builder.gt(rootGt.get("age"), 30));
        List<Customer> gtResult = session.createQuery(gtQuery).getResultList();
        gtResult.forEach(c -> System.out.println(c.getName()));

        // 3. Between Restriction
        System.out.println("\nCustomers aged between 25 and 35:");
        CriteriaQuery<Customer> betweenQuery = builder.createQuery(Customer.class);
        Root<Customer> rootBetween = betweenQuery.from(Customer.class);
        betweenQuery.select(rootBetween).where(builder.between(rootBetween.get("age"), 25, 35));
        List<Customer> betweenResult = session.createQuery(betweenQuery).getResultList();
        betweenResult.forEach(c -> System.out.println(c.getName()));

        // 4. Like Restriction
        System.out.println("\nCustomers whose names start with 'A':");
        CriteriaQuery<Customer> likeQuery = builder.createQuery(Customer.class);
        Root<Customer> rootLike = likeQuery.from(Customer.class);
        likeQuery.select(rootLike).where(builder.like(rootLike.get("name"), "A%"));
        List<Customer> likeResult = session.createQuery(likeQuery).getResultList();
        likeResult.forEach(c -> System.out.println(c.getName()));

        session.close(); // Closing the session
    }
}
