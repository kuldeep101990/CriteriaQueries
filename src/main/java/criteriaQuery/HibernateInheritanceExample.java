package criteriaQuery;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.transform.Transformers;

import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class HibernateInheritanceExample {

    public static void main(String[] args) {
        // Load the configuration and build the SessionFactory
        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml"); // Assuming the XML config is correct
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties())
                .build();
        SessionFactory sessionFactory = configuration.buildSessionFactory(serviceRegistry);

        // Create a session
        try {
        	Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();

            try {
                // Create some Address objects
                Address address1 = new Address("123 delhi St", "Delhi", "DLI", "10001");
                Address address2 = new Address("456 noida St", "Noida", "No", "02115");
                Address address3 = new Address("789 lucknow St", "Uttar pradesh", "UP", "94110");

                // Create some Person objects
                Person person1 = new Person("Kuldeep Kaushik", new Date(80, 3, 15));
                person1.setAddress(address1);
                Person person2 = new Person("Deep Sharma", new Date(85, 8, 25));
                person2.setAddress(address2);
                Person person3 = new Person("Bishal Jankar", new Date(90, 5, 10));
                person3.setAddress(address3);
                Person person4 = new Person("Somi Sharma", new Date(75, 1, 1));
                person4.setAddress(address1);

                // Save the objects to the database
                session.save(address1);
                session.save(address2);
                session.save(address3);
                session.save(person1);
                session.save(person2);
                session.save(person3);
                session.save(person4);

                // Commit the transaction
                tx.commit();


                // 1. Associations
                // Join with the Address entity (correct field name for address relationship)
                Criteria criteria1 = session.createCriteria(Person.class).createAlias("address", "a") // Join Address table
                        .add(Restrictions.eq("a.city", "Delhi")); // Filter by city 'Delhi'

                
                List results1 = criteria1.list();
                // Execute the query and retrieve the results
                System.out.println("results1 "+results1);
                
                // 2. Restrictions
                // Add a restriction on the date of birth (DOB)
                Date startDate = new Date(80, 0, 1);
                Date endDate = new Date(90, 0, 1);
                Criteria criteria2 = session.createCriteria(Person.class).add(Restrictions.between("dob", startDate, endDate));

                List results2 = criteria2.list();
                // Execute the query and retrieve the results
                System.out.println("results2 "+results2);
                
                // 3. Ordering
                // Order the results by name in descending order
                Criteria criteria3 = session.createCriteria(Person.class).addOrder(Order.desc("name"));
                List results3 = criteria3.list();
                // Execute the query and retrieve the results
                System.out.println("results3 "+results3);
                
                // 4. Projections
                // Select the name and date of birth properties and return a list of custom objects
                Criteria criteria4 = session.createCriteria(Person.class).setProjection(Projections.projectionList()
                        .add(Projections.property("name"), "name")
                        .add(Projections.property("dob"), "dob"))
                        .setResultTransformer(Transformers.aliasToBean(PersonSummary.class));

                List<PersonSummary> results4 = criteria4.list();
                
                // Process the results (can be done in any suitable way)
                results4.forEach(new Consumer<PersonSummary>() {
					public void accept(PersonSummary ps) {
						System.out.println("results4 "+ ps);
					}
				});
                
                // 5. Aggregation
                // Count the number of persons in the result set
                Criteria criteria5 = session.createCriteria(Person.class).setProjection(Projections.rowCount());
                List results5 = criteria5.list();
                // Execute the query and retrieve the results
                System.out.println("results5 "+ results5);

               session.close();

            } catch (Exception e) {
                if (tx != null) tx.rollback();
                e.printStackTrace();
            }
        } finally {
            sessionFactory.close(); // Ensure the session factory is closed after execution
        }
    }
}
