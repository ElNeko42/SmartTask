package com.smarttask.taskallocator.infrastructure.config;

import com.smarttask.taskallocator.application.port.out.MemberRepository;
import com.smarttask.taskallocator.application.service.MemberService;
import com.smarttask.taskallocator.application.service.TaskAllocationService;
import com.smarttask.taskallocator.domain.service.AllocationPolicy;
import com.smarttask.taskallocator.domain.service.LeastLoadedAllocationPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Cableado de beans de dominio y aplicación.
 *
 * <p>Al declararse aquí, las clases de {@code domain} y {@code application}
 * permanecen sin anotaciones de Spring: es la infraestructura la que decide
 * cómo se construyen e inyectan. Así se mantiene la dependencia apuntando
 * siempre hacia el dominio.
 */
@Configuration
public class BeanConfiguration {

    @Bean
    public AllocationPolicy allocationPolicy() {
        return new LeastLoadedAllocationPolicy();
    }

    @Bean
    public MemberService memberService(MemberRepository memberRepository) {
        return new MemberService(memberRepository);
    }

    @Bean
    public TaskAllocationService taskAllocationService(MemberRepository memberRepository,
                                                       AllocationPolicy allocationPolicy) {
        return new TaskAllocationService(memberRepository, allocationPolicy);
    }
}
