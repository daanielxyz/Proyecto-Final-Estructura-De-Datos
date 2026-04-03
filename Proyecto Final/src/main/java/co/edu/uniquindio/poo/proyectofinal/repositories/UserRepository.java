package co.edu.uniquindio.poo.proyectofinal.repositories;

import co.edu.uniquindio.poo.proyectofinal.models.User;
import co.edu.uniquindio.poo.proyectofinal.structures.lists.ListaSimple;
import co.edu.uniquindio.poo.proyectofinal.structures.hash.TablaHash;

public class UserRepository {

    private final TablaHash<String, User> users;

    public UserRepository() {
        this.users = new TablaHash<>();
    }

    public void save(User user) {
        users.put(user.getId(), user);
    }

    public User findById(String id) {
        return users.get(id);
    }

    public void delete(String id) {
        if (!users.containsKey(id)) {
            throw new RuntimeException("Usuario no encontrado: " + id);
        }
        users.remove(id);
    }

    public boolean exists(String id) {
        return users.containsKey(id);
    }

    public ListaSimple<User> findAll() {
        return users.values();
    }

    /**
     * Busca un usuario por email. Recorre todos ya que el índice es por ID.
     */
    public User findByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) return user;
        }
        return null;
    }
}