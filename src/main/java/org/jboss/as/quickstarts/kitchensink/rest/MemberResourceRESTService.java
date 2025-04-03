package org.jboss.as.quickstarts.kitchensink.rest;

import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.jboss.as.quickstarts.kitchensink.service.MemberRegistration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

import java.util.*;
import java.util.logging.Logger;

@RestController
@RequestMapping("/rest/members")
public class MemberResourceRESTService {

    private final Logger log;
    private final Validator validator;
    private final MemberRepository repository;
    private final MemberRegistration registration;

    public MemberResourceRESTService(Logger log, Validator validator,
                                     MemberRepository repository,
                                     MemberRegistration registration) {
        this.log = log;
        this.validator = validator;
        this.repository = repository;
        this.registration = registration;
    }

    @GetMapping
    public List<Member> listAllMembers() {
        return repository.findAllOrderedByName();
    }

    @GetMapping("/{id}")
    public Member lookupMemberById(@PathVariable("id") long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<?> createMember(@RequestBody Member member) {
        try {
            // Validate the incoming member
            validateMember(member);

            // Register the member
            registration.register(member);

            return ResponseEntity.ok().build();
        } catch (ConstraintViolationException ce) {
            return ResponseEntity.badRequest().body(createViolationMap(ce.getConstraintViolations()));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("email", "Email taken"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private void validateMember(Member member) throws ConstraintViolationException, ValidationException {
        Set<ConstraintViolation<Member>> violations = validator.validate(member);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(new HashSet<>(violations));
        }

        if (emailAlreadyExists(member.getEmail())) {
            throw new ValidationException("Unique Email Violation");
        }
    }

    private Map<String, String> createViolationMap(Set<ConstraintViolation<?>> violations) {
        log.fine("Validation completed. Violations found: " + violations.size());

        Map<String, String> responseObj = new HashMap<>();
        for (ConstraintViolation<?> violation : violations) {
            responseObj.put(violation.getPropertyPath().toString(), violation.getMessage());
        }

        return responseObj;
    }

    private boolean emailAlreadyExists(String email) {
        try {
            return repository.findByEmail(email) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
