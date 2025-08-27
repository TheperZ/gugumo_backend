package sideproject.gugumo.auditor;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import sideproject.gugumo.domain.dto.memberDto.CustomUserDetails;

import java.util.Optional;

@Component
public class MemberAuditorAware implements AuditorAware<Long> {
    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails)) {
            return Optional.empty();
        }

        CustomUserDetails principal = (CustomUserDetails) authentication.getPrincipal();

        return Optional.of(principal.getId());
    }
}