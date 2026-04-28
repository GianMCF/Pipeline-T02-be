package pe.edu.vallegrande.vinumawbe.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import pe.edu.vallegrande.vinumawbe.model.Employee;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
}
