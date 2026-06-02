package pe.edu.vallegrande.vinumawbe.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pe.edu.vallegrande.vinumawbe.model.Employee;
import pe.edu.vallegrande.vinumawbe.repository.EmployeeRepository;
import pe.edu.vallegrande.vinumawbe.service.EmployeeService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Slf4j
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Flux<Employee> findAll() {
        log.info("Mostrando datos");
        return employeeRepository.findAll();
    }

    @Override
    public Mono<Employee> findById(String id) {
        log.info("Mostrando datos por ID ");
        return employeeRepository.findById(id);
    }

    @Override
    public Mono<Employee> save(Employee employee) {
        log.info("Registrando datos " + employee.toString());
        employee.setStatus(true);
        employee.setAddedAt(LocalDateTime.now());
        return employeeRepository.save(employee);
    }

    @Override
    public Mono<Employee> update(Employee employee) {
        log.info("Actualizando datos " + employee.toString());
        employee.setStatus(true);
        employee.setUpdatedAt(LocalDateTime.now());
        return employeeRepository.save(employee);
    }
    @Override
    public Mono<Employee> delete(String id) {

        log.info("Eliminando cliente (lógico) con id: " + id);

        return employeeRepository.findById(id)
                .flatMap(employee -> {
                    employee.setStatus(false);
                    employee.setDeletedAt(LocalDateTime.now());
                    return employeeRepository.save(employee);
                });
    }

    @Override
    public Mono<Employee> restore(String id) {

        log.info("Restaurando cliente (lógico) con id: " + id);

        return employeeRepository.findById(id)
                .flatMap(employee -> {
                    employee.setStatus(true);
                    employee.setRestoredAt(LocalDateTime.now());
                    return employeeRepository.save(employee);
                });
    }
}
