package pe.edu.vallegrande.vinumawbe.service;

import pe.edu.vallegrande.vinumawbe.model.Employee;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
    Flux<Employee> findAll();
    Mono<Employee> findById(String id);
    Mono<Employee> save(Employee employee);
    Mono<Employee> update(Employee employee);
    Mono<Employee> delete(String id);
    Mono<Employee> restore(String id);
}
