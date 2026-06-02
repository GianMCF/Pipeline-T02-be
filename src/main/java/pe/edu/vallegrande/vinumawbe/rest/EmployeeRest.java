package pe.edu.vallegrande.vinumawbe.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.vallegrande.vinumawbe.model.Employee;
import pe.edu.vallegrande.vinumawbe.service.EmployeeService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/api/employee")
@Tag(name = "Empleados", description = "CRUD reactivo de empleados")
public class EmployeeRest {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeRest(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(summary = "Listar todos los empleados", description = "Lista a todos los empleados en la base de datos")
    @GetMapping
    public Flux<Employee> findAll() {
        return employeeService.findAll();
    }

    @Operation(summary = "Hallar empleado por ID", description = "Lista a un solo empleado en la base de datos")
    @GetMapping("/{id}")
    public Mono<Employee> findById(@PathVariable String id) {
        return employeeService.findById(id);
    }

    @Operation(summary = "Registrar empleado", description = "Registra un nuevo empleado en la base de datos")
    @PostMapping("/save")
    public Mono<Employee> save(@RequestBody Employee employee) {
        return employeeService.save(employee);
    }

    @Operation(summary = "Actualizar empleado", description = "Modifica datos de un empleado en la base de datos")
    @PutMapping ("/update")
    public Mono<Employee> update(@RequestBody Employee employee) {
        return employeeService.update(employee);
    }

    @Operation(summary = "Eliminar lógicamente a un empleado", description = "Desactivar a un empleado en la base de datos")
    @DeleteMapping("/delete/{id}")
    public Mono<Employee> delete(@PathVariable String id) {
        return employeeService.delete(id);
    }

    @Operation(summary = "Restaurar lógicamente a un empleado", description = "Re-activar a un empleado en la base de datos")
    @PutMapping("/restore/{id}")
    public Mono<Employee> restore(@PathVariable String id) {
        return employeeService.restore(id);
    }
}
