package xc.investigation.base;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.persistence.EntityManager;

/**
 * @author ibm
 */
@SpringBootApplication
public class BaseApplication {

	public BaseApplication(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public static void main(String[] args) {
		SpringApplication.run(BaseApplication.class, args);
	}

	private final EntityManager entityManager;

	@Bean
	public JPAQueryFactory jpaQueryFactory(){
		return new JPAQueryFactory(entityManager);
	}
}
