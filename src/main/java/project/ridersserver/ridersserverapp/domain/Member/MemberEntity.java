package project.ridersserver.ridersserverapp.domain.Member;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import javafx.scene.NodeBuilder;
import lombok.*;
import org.springframework.util.Assert;

@SequenceGenerator(name = "MEMBER_SEQ_GENERATOR", sequenceName = "MEMBER_SEQ", initialValue = 1, allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@Getter
@Setter
@Entity
@Table(name = "member")
public class MemberEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE , generator="MEMBER_SEQ_GENERATOR")
    private Long id;

    @Column(length = 20, nullable = false , unique = true)
    private String email;

    @Column(length = 100, nullable = false)
    private String password;



}