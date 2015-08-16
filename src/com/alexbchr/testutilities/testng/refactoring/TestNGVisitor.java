package com.alexbchr.testutilities.testng.refactoring;

import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Sets;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.testng.collections.Maps;
import com.alexbchr.testutilities.testng.ui.conversion.Visitor;

/**
 * The visitor for a TestNG class.
 *
 * @author Cedric Beust <cedric@beust.com>
 */
public class TestNGVisitor extends Visitor {

  private Map<MethodDeclaration, Annotation> m_testMethods = Maps.newHashMap();
  private TypeDeclaration m_type;
  private Annotation m_testClassAnnotation;
  private Set<MethodDeclaration> m_publicMethods = Sets.newHashSet();

  // List of assert calls, so we can suggest static imports for them
  private Set<String> m_assertMethods = Sets.newHashSet();

  @Override
  public boolean visit(MethodDeclaration md) {
    
    if (hasAnnotation(md, "Test")) {
      m_testMethods.put(md, getAnnotation(md, "Test"));
    }

    if ((md.getModifiers() & Modifier.PUBLIC) != 0) {
      @SuppressWarnings("unchecked")
      boolean hasTestNGAnnotation = false;
      List<IExtendedModifier> modifiers = md.modifiers();
      for (IExtendedModifier m : modifiers) {
        if (m.isAnnotation()) {
          Annotation a = (Annotation) m;
          IAnnotationBinding ab = a.resolveAnnotationBinding();
          String typeName = ab.getAnnotationType().getBinaryName();
          if (typeName.contains("org.testng")) {
            hasTestNGAnnotation = true;
            break;
          }
        }
      }
      if (! hasTestNGAnnotation) m_publicMethods.add(md);
    }

    return super.visit(md);
  }

  /**
   * Record whether this type declaration is a TestCase or a TestSuite.
   */
  @Override
  public boolean visit(TypeDeclaration td) {
    m_type = td;
    m_testClassAnnotation = getAnnotation(td, "Test");
    return super.visit(td);
  }

  @Override
  public boolean visit(MethodInvocation mi) {
    if (mi.getName().toString().startsWith("assert")) {
      if (mi.resolveMethodBinding() == null) m_assertMethods.add(mi.getName().toString());
    }
    return super.visit(mi);
  }

  public Set<String> getAsserts() {
    return m_assertMethods;
  }

  public Map<MethodDeclaration, Annotation> getTestMethods() {
    return m_testMethods;
  }

  public TypeDeclaration getType() {
    return m_type;
  }

  public Annotation getTestClassAnnotation() {
    return m_testClassAnnotation;
  }

  /**
   * @return The public methods that don't have a TestNG annotation on them.
   */
  public Set<MethodDeclaration> getPublicMethods() {
    return m_publicMethods;
  }
}
