package co.edu.uniquindio.poo.proyectofinal.services;

import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.models.enums.UserLevel;
import co.edu.uniquindio.poo.proyectofinal.repositories.UserRepository;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;
import co.edu.uniquindio.poo.proyectofinal.utils.IDGenerator;

public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User register(String fullName, String email, String phone, String password) {
        if (fullName == null || fullName.isBlank())
            throw new RuntimeException("El nombre no puede estar vacío.");
        if (email == null || email.isBlank())
            throw new RuntimeException("El email no puede estar vacío.");
        if (userRepository.findByEmail(email) != null)
            throw new RuntimeException("Ya existe un usuario con ese email.");
        if (password == null || password.length() < 6)
            throw new RuntimeException("La contraseña debe tener al menos 6 caracteres.");

        String id = IDGenerator.generateUserId();
        User user = new User(id, fullName, email, phone, password);
        userRepository.save(user);
        return user;
    }

    public User findById(String id) {
        User user = userRepository.findById(id);
        if (user == null) throw new RuntimeException("Usuario no encontrado: " + id);
        return user;
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) throw new RuntimeException("Usuario no encontrado: " + email);
        return user;
    }

    public ListaSimple<User> findAll() {
        return userRepository.findAll();
    }

    public User update(String id, String fullName, String phone) {
        User user = findById(id);
        if (fullName != null && !fullName.isBlank()) user.setFullName(fullName);
        if (phone != null && !phone.isBlank()) user.setPhone(phone);
        userRepository.save(user);
        return user;
    }

    public void deactivate(String id) {
        User user = findById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    public void delete(String id) {
        findById(id); // valida que exista
        userRepository.delete(id);
    }

    /**
     * Agrega puntos al usuario y retorna true si subió de nivel.
     */
    public boolean addPoints(String userId, int points) {
        User user = findById(userId);
        UserLevel levelBefore = user.getLevel();
        user.addPoints(points);
        userRepository.save(user);
        return user.getLevel() != levelBefore;
    }

    /**
     * Descuenta puntos al usuario (reversión o canje de beneficio).
     */
    public void subtractPoints(String userId, int points) {
        User user = findById(userId);
        user.subtractPoints(points);
        userRepository.save(user);
    }

    public boolean validatePassword(String userId, String password) {
        User user = findById(userId);
        return user.getPasswordHash().equals(password);
    }
}