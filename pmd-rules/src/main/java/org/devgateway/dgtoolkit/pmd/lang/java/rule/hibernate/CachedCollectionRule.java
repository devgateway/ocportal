package org.devgateway.dgtoolkit.pmd.lang.java.rule.hibernate;

import net.sourceforge.pmd.lang.java.ast.ASTAnnotation;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * @author Octavian Ciubotaru
 */
public class CachedCollectionRule extends AbstractJavaRule {

    public CachedCollectionRule() {
        addRuleChainVisit(ASTAnnotation.class);
    }

    @Override
    public Object visit(ASTAnnotation node, Object data) {
        if (node.getParent() instanceof ASTClassOrInterfaceBodyDeclaration
                && isCollectionAnnotation(node)
                && !isCached((ASTClassOrInterfaceBodyDeclaration) node.getParent())) {
            addViolation(data, node.getParent().getFirstChildOfType(ASTFieldDeclaration.class));
        }
        return data;
    }

    private boolean isCached(ASTClassOrInterfaceBodyDeclaration node) {
        return node.findChildrenOfType(ASTAnnotation.class).stream().anyMatch(this::isCacheAnnotation);
    }

    private boolean isCollectionAnnotation(ASTAnnotation a) {
        return a.getAnnotationName().equals("OneToMany")
                || a.getAnnotationName().equals("jakarta.persistence.OneToMany")
                || a.getAnnotationName().equals("ManyToMany")
                || a.getAnnotationName().equals("jakarta.persistence.ManyToMany");
    }

    private boolean isCacheAnnotation(ASTAnnotation a) {
        return a.getAnnotationName().equals("Cache")
                || a.getAnnotationName().equals("org.hibernate.annotations.Cache");
    }
}
