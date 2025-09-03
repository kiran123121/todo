package com.rkinfo;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@Service
public class TodoService {

    @Autowired
    private TodoRepository todoRepository;

    public List<Todo> getTodosByUser(String userId) {
        return todoRepository.findByUserId(userId);
    }

    public Todo save(Todo todo) {
        return todoRepository.save(todo);
    }

    public void delete(String id) {
        todoRepository.deleteById(id);
    }

    public Todo findById(String id) {
        return todoRepository.findById(id).orElse(null);
    }
}
