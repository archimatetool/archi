package com.archimatetool.model;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Folder Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.archimatetool.model.IArchimatePackage#getFolderType()
 * @model
 * @generated
 */
public enum FolderType implements Enumerator {
    /**
     * The '<em><b>User</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #USER_VALUE
     * @generated NOT
     * @ordered
     */
    USER(0, "user", "user", Messages.FolderType_0), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>Strategy</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #STRATEGY_VALUE
     * @generated NOT
     * @ordered
     */
    STRATEGY(1, "strategy", "strategy", Messages.FolderType_1), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>Business</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BUSINESS_VALUE
     * @generated NOT
     * @ordered
     */
    BUSINESS(2, "business", "business", Messages.FolderType_2), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>Application</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #APPLICATION_VALUE
     * @generated NOT
     * @ordered
     */
    APPLICATION(3, "application", "application", Messages.FolderType_3), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>Technology</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #TECHNOLOGY_VALUE
     * @generated NOT
     * @ordered
     */
    TECHNOLOGY(4, "technology", "technology", Messages.FolderType_4), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>Relations</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #RELATIONS_VALUE
     * @generated NOT
     * @ordered
     */
    RELATIONS(5, "relations", "relations", Messages.FolderType_5), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>Other</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #OTHER_VALUE
     * @generated NOT
     * @ordered
     */
    OTHER(6, "other", "other", Messages.FolderType_6), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>Diagrams</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #DIAGRAMS_VALUE
     * @generated NOT
     * @ordered
     */
    DIAGRAMS(7, "diagrams", "diagrams", Messages.FolderType_7), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>Motivation</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #MOTIVATION_VALUE
     * @generated NOT
     * @ordered
     */
    MOTIVATION(8, "motivation", "motivation", Messages.FolderType_8), //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>Implementation migration</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #IMPLEMENTATION_MIGRATION_VALUE
     * @generated NOT
     * @ordered
     */
    IMPLEMENTATION_MIGRATION(9, "implementation_migration", "implementation_migration", Messages.FolderType_9); //$NON-NLS-1$ //$NON-NLS-2$

    /**
     * The '<em><b>User</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>User</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #USER
     * @model name="user"
     * @generated
     * @ordered
     */
    public static final int USER_VALUE = 0;

    /**
     * The '<em><b>Strategy</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Strategy</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #STRATEGY
     * @model name="strategy"
     * @generated
     * @ordered
     */
    public static final int STRATEGY_VALUE = 1;

    /**
     * The '<em><b>Business</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Business</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #BUSINESS
     * @model name="business"
     * @generated
     * @ordered
     */
    public static final int BUSINESS_VALUE = 2;

    /**
     * The '<em><b>Application</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Application</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #APPLICATION
     * @model name="application"
     * @generated
     * @ordered
     */
    public static final int APPLICATION_VALUE = 3;

    /**
     * The '<em><b>Technology</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Technology</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #TECHNOLOGY
     * @model name="technology"
     * @generated
     * @ordered
     */
    public static final int TECHNOLOGY_VALUE = 4;

    /**
     * The '<em><b>Relations</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Relations</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #RELATIONS
     * @model name="relations"
     * @generated
     * @ordered
     */
    public static final int RELATIONS_VALUE = 5;

    /**
     * The '<em><b>Other</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Other</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #OTHER
     * @model name="other"
     * @generated
     * @ordered
     */
    public static final int OTHER_VALUE = 6;

    /**
     * The '<em><b>Diagrams</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Diagrams</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #DIAGRAMS
     * @model name="diagrams"
     * @generated
     * @ordered
     */
    public static final int DIAGRAMS_VALUE = 7;

    /**
     * The '<em><b>Motivation</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Motivation</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #MOTIVATION
     * @model name="motivation"
     * @generated
     * @ordered
     */
    public static final int MOTIVATION_VALUE = 8;

    /**
     * The '<em><b>Implementation migration</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Implementation migration</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #IMPLEMENTATION_MIGRATION
     * @model name="implementation_migration"
     * @generated
     * @ordered
     */
    public static final int IMPLEMENTATION_MIGRATION_VALUE = 9;

    /**
     * An array of all the '<em><b>Folder Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final FolderType[] VALUES_ARRAY =
        new FolderType[] {
            USER,
            STRATEGY,
            BUSINESS,
            APPLICATION,
            TECHNOLOGY,
            RELATIONS,
            OTHER,
            DIAGRAMS,
            MOTIVATION,
            IMPLEMENTATION_MIGRATION,
        };

    /**
     * A public read-only list of all the '<em><b>Folder Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<FolderType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Folder Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param literal the literal.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static FolderType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            FolderType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Folder Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param name the name.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static FolderType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            FolderType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Folder Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the integer value.
     * @return the matching enumerator or <code>null</code>.
     * @generated
     */
    public static FolderType get(int value) {
        switch (value) {
            case USER_VALUE: return USER;
            case STRATEGY_VALUE: return STRATEGY;
            case BUSINESS_VALUE: return BUSINESS;
            case APPLICATION_VALUE: return APPLICATION;
            case TECHNOLOGY_VALUE: return TECHNOLOGY;
            case RELATIONS_VALUE: return RELATIONS;
            case OTHER_VALUE: return OTHER;
            case DIAGRAMS_VALUE: return DIAGRAMS;
            case MOTIVATION_VALUE: return MOTIVATION;
            case IMPLEMENTATION_MIGRATION_VALUE: return IMPLEMENTATION_MIGRATION;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;
    
    private String label;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private FolderType(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    private FolderType(int value, String name, String literal, String label) {
        this.value = value;
        this.name = name;
        this.literal = literal;
        this.label = label;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public int getValue() {
      return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getName() {
      return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getLiteral() {
      return literal;
    }
    
    public String getLabel() {
        return label;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }
    
} //FolderType