package byrybdyk.me.lb1.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
public class InfoController {

    public record DataItem(String id, String text) {}

    public record UserDetails(String description, String state) {}

    @GetMapping("/data")
    public ResponseEntity<List<DataItem>> fetchItems() {

        String escaped = HtmlUtils.htmlEscape(
                "Sample data 1 <script>alert('xss')</script>"
        );

        List<DataItem> payload = List.of(
                new DataItem("1", escaped)
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(payload);
    }

    @PostMapping("/data")
    public ResponseEntity<String> storeItem(@RequestBody String rawContent) {

        String cleaned = HtmlUtils.htmlEscape(rawContent);

        String answer = "Incoming content processed: " + cleaned;

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(answer);
    }

    @GetMapping("/user-info")
    public ResponseEntity<UserDetails> loadUserDetails() {

        UserDetails info = new UserDetails(
                "Protected user information is available",
                "authenticated"
        );

        return ResponseEntity.ok(info);
    }
}
