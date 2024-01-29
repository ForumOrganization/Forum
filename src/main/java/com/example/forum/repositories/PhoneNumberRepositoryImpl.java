package com.example.forum.repositories;

import com.example.forum.exceptions.EntityNotFoundException;
import com.example.forum.models.Comment;
import com.example.forum.models.PhoneNumber;
import com.example.forum.models.Reaction_comments;
import com.example.forum.models.User;
import com.example.forum.repositories.contracts.PhoneNumberRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PhoneNumberRepositoryImpl implements PhoneNumberRepository {

    private SessionFactory sessionFactory;

    public PhoneNumberRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public PhoneNumber getPhoneNumberByUserId(int userId) {
        try (Session session = sessionFactory.openSession()) {
            PhoneNumber phoneNumber = session.get(PhoneNumber.class, userId);

//            Query<PhoneNumber> query = session.createQuery("SELECT p FROM PhoneNumber p WHERE p.user.id = :userId", PhoneNumber.class);
//            query.setParameter("userId", userId);

            if (phoneNumber == null) {
                throw new EntityNotFoundException("Admin", "phone number");
            }

            return phoneNumber;
        }
    }

    @Override
    public void addPhoneNumberToAdmin(User admin, PhoneNumber phone) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(phone);
            session.getTransaction().commit();
        }
    }

    @Override
    public void updatePhoneNumber(PhoneNumber phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(phoneNumber);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deletePhoneNumber(int userId) {
        PhoneNumber phoneNumberToDelete = getPhoneNumberByUserId(userId);

        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(phoneNumberToDelete);
            session.getTransaction().commit();
        }
    }
}