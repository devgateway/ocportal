package org.devgateway.dgtoolkit.pmd.lang.java.rule.hibernate;

import net.sourceforge.pmd.lang.java.ast.ASTAnnotation;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * @author Octavian Ciubotaru
 */
public class CachedToOneRule extends AbstractJavaRule {

    public CachedToOneRule() {
        addRuleChainVisit(ASTAnnotation.class);
    }

    @Override
    public Object visit(ASTAnnotation node, Object data) {
        if (node.getParent() instanceof ASTClassOrInterfaceBodyDeclaration
                && isToOneAnnotation(node)
                && isCached((ASTClassOrInterfaceBodyDeclaration) node.getParent())) {
            addViolation(data, node.getParent().getFirstChildOfType(ASTFieldDeclaration.class));
        }
        return data;
    }

    private boolean isCached(ASTClassOrInterfaceBodyDeclaration node) {
        return node.findChildrenOfType(ASTAnnotation.class).stream().anyMatch(this::isCacheAnnotation);
    }

    private boolean isToOneAnnotation(ASTAnnotation a) {
        return a.getAnnotationName().equals("OneToOne")
                || a.getAnnotationName().equals("jakarta.persistence.OneToOne")
                || a.getAnnotationName().equals("ManyToOne")
                || a.getAnnotationName().equals("jakarta.persistence.ManyToOne");
    }

    private boolean isCacheAnnotation(ASTAnnotation a) {
        return a.getAnnotationName().equals("Cache")
                || a.getAnnotationName().equals("org.hibernate.annotations.Cache");
    }
}
