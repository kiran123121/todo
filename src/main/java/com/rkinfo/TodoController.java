package com.rkinfo;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/todos")
public class TodoController {

    private final TodoRepository todoRepo;
    private final UserRepository userRepo;

    public TodoController(TodoRepository todoRepo, UserRepository userRepo) {
        this.todoRepo = todoRepo;
        this.userRepo = userRepo;
    }

    // ✅ Show todos
    @GetMapping
    public String listTodos(HttpSession session, Model model) {
        User u = (User) session.getAttribute("user");  // <-- must match AuthController
        if (u == null) {
            return "redirect:/login";  // avoid null pointer crash
        }

        // Defensive: check ID
        if (u.getId() == null) {
            System.out.println("DEBUG: Logged user has no ID. Did you save them properly in Mongo?");
            return "redirect:/login";
        }

        model.addAttribute("todos", todoRepo.findByUserId(u.getId()));
        model.addAttribute("newTodo", new Todo());
        return "todo-list";   // <-- must match your thymeleaf template filename
    }


    // ✅ Add todo
    @PostMapping
    public String addTodo(@ModelAttribute Todo newTodo, HttpSession session) {
        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        // Always assign userId so todos don’t disappear
        newTodo.setId(null); // force MongoDB to generate new ID
        newTodo.setUserId(u.getId());
        newTodo.setCompleted(false);

        todoRepo.save(newTodo);
        return "redirect:/todos";
    }

    // ✅ Delete todo
    @PostMapping("/delete/{id}")
    public String deleteTodo(@PathVariable String id, HttpSession session) {
        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        Optional<Todo> todo = todoRepo.findById(id);
        if (todo.isPresent() && u.getId().equals(todo.get().getUserId())) {
            todoRepo.deleteById(id);
        }
        return "redirect:/todos";
    }

    // ✅ Mark completed
    @PostMapping("/complete/{id}")
    public String completeTodo(@PathVariable String id, HttpSession session) {
        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        Optional<Todo> todo = todoRepo.findById(id);
        if (todo.isPresent() && u.getId().equals(todo.get().getUserId())) {
            Todo t = todo.get();
            t.setCompleted(true);
            todoRepo.save(t);
        }
        return "redirect:/todos";
    }

    // ✅ Edit todo
    @PostMapping("/edit/{id}")
    public String editTodo(@PathVariable String id,
                           @RequestParam String task,
                           HttpSession session) {
        User u = (User) session.getAttribute("user");
        if (u == null) return "redirect:/login";

        Optional<Todo> todo = todoRepo.findById(id);
        if (todo.isPresent() && u.getId().equals(todo.get().getUserId())) {
            Todo t = todo.get();
            t.setTask(task);
            todoRepo.save(t);
        }
        return "redirect:/todos";
    }
}
