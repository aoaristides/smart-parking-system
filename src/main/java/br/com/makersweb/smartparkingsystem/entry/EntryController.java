package br.com.makersweb.smartparkingsystem.entry;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author andersonorione
 */
@RestController
@RequestMapping("/parking")
public class EntryController {

    private final EntryService entryService;
    private final ExitService exitService;

    public EntryController(final EntryService entryService, final ExitService exitService) {
        this.entryService = entryService;
        this.exitService = exitService;
    }

    @PostMapping("/entry")
    public ResponseEntity<String> entry(final @RequestParam String vehicleNumber) {
        entryService.vehicleEntry(vehicleNumber);
        return ResponseEntity.ok("Vehicle entered: " + vehicleNumber);
    }

    @PostMapping("/exit")
    public ResponseEntity<String> exit(final @RequestParam String vehicleNumber) {
        exitService.vehicleExit(vehicleNumber);
        return ResponseEntity.ok("Vehicle exited: " + vehicleNumber);
    }

}
