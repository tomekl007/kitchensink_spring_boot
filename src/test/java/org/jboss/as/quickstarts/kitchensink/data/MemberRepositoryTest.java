import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.*;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jboss.as.quickstarts.kitchensink.data.MemberRepository;
import org.jboss.as.quickstarts.kitchensink.model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

@ApplicationScoped
public class MemberRepositoryTest {

    @Inject
    @Mock
    private EntityManager em;

    @InjectMocks
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindById() {
        Member mockMember = new Member();
        mockMember.setId(1L);

        when(em.find(Member.class, 1L)).thenReturn(mockMember);

        Member result = memberRepository.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(em).find(Member.class, 1L);
    }

    @Test
    void testFindByEmail() {
        String email = "test@example.com";
        Member mockMember = new Member();
        mockMember.setEmail(email);

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Member> cq = mock(CriteriaQuery.class);
        Root<Member> root = mock(Root.class);
        TypedQuery<Member> tq = mock(TypedQuery.class);

        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Member.class)).thenReturn(cq);
        when(cq.from(Member.class)).thenReturn(root);
        when(cb.equal(any(), eq(email))).thenReturn(mock(Predicate.class));
        when(cq.select(root)).thenReturn(cq);
        when(cq.where(any(Predicate.class))).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);
        when(tq.getSingleResult()).thenReturn(mockMember);

        Member result = memberRepository.findByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void testFindAllOrderedByName() {
        List<Member> mockList = List.of(new Member(), new Member());

        CriteriaBuilder cb = mock(CriteriaBuilder.class);
        CriteriaQuery<Member> cq = mock(CriteriaQuery.class);
        Root<Member> root = mock(Root.class);
        TypedQuery<Member> tq = mock(TypedQuery.class);

        when(em.getCriteriaBuilder()).thenReturn(cb);
        when(cb.createQuery(Member.class)).thenReturn(cq);
        when(cq.from(Member.class)).thenReturn(root);
        when(cb.asc(any())).thenReturn(mock(Order.class));
        when(cq.select(root)).thenReturn(cq);
        when(cq.orderBy(any(Order.class))).thenReturn(cq);
        when(em.createQuery(cq)).thenReturn(tq);
        when(tq.getResultList()).thenReturn(mockList);

        List<Member> result = memberRepository.findAllOrderedByName();

        assertEquals(2, result.size());
    }
}
