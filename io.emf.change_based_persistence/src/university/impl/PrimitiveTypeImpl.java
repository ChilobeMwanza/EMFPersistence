/**
 */
package university.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import university.PrimitiveType;
import university.UniversityPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Primitive Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getA <em>A</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getB <em>B</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getC <em>C</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getBigIntList <em>Big Int List</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#isD <em>D</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getE <em>E</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getF <em>F</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getG <em>G</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getH <em>H</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getI <em>I</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getJ <em>J</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getK <em>K</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getL <em>L</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getM <em>M</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getN <em>N</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getO <em>O</em>}</li>
 *   <li>{@link university.impl.PrimitiveTypeImpl#getP <em>P</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PrimitiveTypeImpl extends MinimalEObjectImpl.Container implements PrimitiveType {
	/**
	 * The default value of the '{@link #getA() <em>A</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getA()
	 * @generated
	 * @ordered
	 */
	protected static final String A_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getA() <em>A</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getA()
	 * @generated
	 * @ordered
	 */
	protected String a = A_EDEFAULT;

	/**
	 * The default value of the '{@link #getB() <em>B</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getB()
	 * @generated
	 * @ordered
	 */
	protected static final int B_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getB() <em>B</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getB()
	 * @generated
	 * @ordered
	 */
	protected int b = B_EDEFAULT;

	/**
	 * The default value of the '{@link #getC() <em>C</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getC()
	 * @generated
	 * @ordered
	 */
	protected static final Integer C_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getC() <em>C</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getC()
	 * @generated
	 * @ordered
	 */
	protected Integer c = C_EDEFAULT;

	/**
	 * The cached value of the '{@link #getBigIntList() <em>Big Int List</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBigIntList()
	 * @generated
	 * @ordered
	 */
	protected EList<Integer> bigIntList;

	/**
	 * The default value of the '{@link #isD() <em>D</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isD()
	 * @generated
	 * @ordered
	 */
	protected static final boolean D_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isD() <em>D</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isD()
	 * @generated
	 * @ordered
	 */
	protected boolean d = D_EDEFAULT;

	/**
	 * The default value of the '{@link #getE() <em>E</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getE()
	 * @generated
	 * @ordered
	 */
	protected static final Boolean E_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getE() <em>E</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getE()
	 * @generated
	 * @ordered
	 */
	protected Boolean e = E_EDEFAULT;

	/**
	 * The default value of the '{@link #getF() <em>F</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getF()
	 * @generated
	 * @ordered
	 */
	protected static final float F_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getF() <em>F</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getF()
	 * @generated
	 * @ordered
	 */
	protected float f = F_EDEFAULT;

	/**
	 * The default value of the '{@link #getG() <em>G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getG()
	 * @generated
	 * @ordered
	 */
	protected static final Float G_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getG() <em>G</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getG()
	 * @generated
	 * @ordered
	 */
	protected Float g = G_EDEFAULT;

	/**
	 * The default value of the '{@link #getH() <em>H</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getH()
	 * @generated
	 * @ordered
	 */
	protected static final Double H_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getH() <em>H</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getH()
	 * @generated
	 * @ordered
	 */
	protected Double h = H_EDEFAULT;

	/**
	 * The default value of the '{@link #getI() <em>I</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getI()
	 * @generated
	 * @ordered
	 */
	protected static final double I_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getI() <em>I</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getI()
	 * @generated
	 * @ordered
	 */
	protected double i = I_EDEFAULT;

	/**
	 * The default value of the '{@link #getJ() <em>J</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJ()
	 * @generated
	 * @ordered
	 */
	protected static final byte J_EDEFAULT = 0x00;

	/**
	 * The cached value of the '{@link #getJ() <em>J</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getJ()
	 * @generated
	 * @ordered
	 */
	protected byte j = J_EDEFAULT;

	/**
	 * The default value of the '{@link #getK() <em>K</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getK()
	 * @generated
	 * @ordered
	 */
	protected static final Byte K_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getK() <em>K</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getK()
	 * @generated
	 * @ordered
	 */
	protected Byte k = K_EDEFAULT;

	/**
	 * The default value of the '{@link #getL() <em>L</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getL()
	 * @generated
	 * @ordered
	 */
	protected static final short L_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getL() <em>L</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getL()
	 * @generated
	 * @ordered
	 */
	protected short l = L_EDEFAULT;

	/**
	 * The default value of the '{@link #getM() <em>M</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getM()
	 * @generated
	 * @ordered
	 */
	protected static final Short M_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getM() <em>M</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getM()
	 * @generated
	 * @ordered
	 */
	protected Short m = M_EDEFAULT;

	/**
	 * The default value of the '{@link #getN() <em>N</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getN()
	 * @generated
	 * @ordered
	 */
	protected static final long N_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getN() <em>N</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getN()
	 * @generated
	 * @ordered
	 */
	protected long n = N_EDEFAULT;

	/**
	 * The default value of the '{@link #getO() <em>O</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getO()
	 * @generated
	 * @ordered
	 */
	protected static final Long O_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getO() <em>O</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getO()
	 * @generated
	 * @ordered
	 */
	protected Long o = O_EDEFAULT;

	/**
	 * The default value of the '{@link #getP() <em>P</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getP()
	 * @generated
	 * @ordered
	 */
	protected static final char P_EDEFAULT = '\u0000';

	/**
	 * The cached value of the '{@link #getP() <em>P</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getP()
	 * @generated
	 * @ordered
	 */
	protected char p = P_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PrimitiveTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return UniversityPackage.Literals.PRIMITIVE_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getA() {
		return a;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setA(String newA) {
		String oldA = a;
		a = newA;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__A, oldA, a));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public int getB() {
		return b;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setB(int newB) {
		int oldB = b;
		b = newB;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__B, oldB, b));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Integer getC() {
		return c;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setC(Integer newC) {
		Integer oldC = c;
		c = newC;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__C, oldC, c));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<Integer> getBigIntList() {
		if (bigIntList == null) {
			bigIntList = new EDataTypeUniqueEList<Integer>(Integer.class, this, UniversityPackage.PRIMITIVE_TYPE__BIG_INT_LIST);
		}
		return bigIntList;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isD() {
		return d;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setD(boolean newD) {
		boolean oldD = d;
		d = newD;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__D, oldD, d));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Boolean getE() {
		return e;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setE(Boolean newE) {
		Boolean oldE = e;
		e = newE;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__E, oldE, e));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public float getF() {
		return f;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setF(float newF) {
		float oldF = f;
		f = newF;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__F, oldF, f));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Float getG() {
		return g;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setG(Float newG) {
		Float oldG = g;
		g = newG;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__G, oldG, g));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Double getH() {
		return h;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setH(Double newH) {
		Double oldH = h;
		h = newH;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__H, oldH, h));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public double getI() {
		return i;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setI(double newI) {
		double oldI = i;
		i = newI;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__I, oldI, i));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public byte getJ() {
		return j;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setJ(byte newJ) {
		byte oldJ = j;
		j = newJ;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__J, oldJ, j));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Byte getK() {
		return k;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setK(Byte newK) {
		Byte oldK = k;
		k = newK;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__K, oldK, k));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public short getL() {
		return l;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setL(short newL) {
		short oldL = l;
		l = newL;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__L, oldL, l));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Short getM() {
		return m;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setM(Short newM) {
		Short oldM = m;
		m = newM;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__M, oldM, m));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getN() {
		return n;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setN(long newN) {
		long oldN = n;
		n = newN;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__N, oldN, n));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Long getO() {
		return o;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setO(Long newO) {
		Long oldO = o;
		o = newO;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__O, oldO, o));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public char getP() {
		return p;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setP(char newP) {
		char oldP = p;
		p = newP;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, UniversityPackage.PRIMITIVE_TYPE__P, oldP, p));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case UniversityPackage.PRIMITIVE_TYPE__A:
				return getA();
			case UniversityPackage.PRIMITIVE_TYPE__B:
				return getB();
			case UniversityPackage.PRIMITIVE_TYPE__C:
				return getC();
			case UniversityPackage.PRIMITIVE_TYPE__BIG_INT_LIST:
				return getBigIntList();
			case UniversityPackage.PRIMITIVE_TYPE__D:
				return isD();
			case UniversityPackage.PRIMITIVE_TYPE__E:
				return getE();
			case UniversityPackage.PRIMITIVE_TYPE__F:
				return getF();
			case UniversityPackage.PRIMITIVE_TYPE__G:
				return getG();
			case UniversityPackage.PRIMITIVE_TYPE__H:
				return getH();
			case UniversityPackage.PRIMITIVE_TYPE__I:
				return getI();
			case UniversityPackage.PRIMITIVE_TYPE__J:
				return getJ();
			case UniversityPackage.PRIMITIVE_TYPE__K:
				return getK();
			case UniversityPackage.PRIMITIVE_TYPE__L:
				return getL();
			case UniversityPackage.PRIMITIVE_TYPE__M:
				return getM();
			case UniversityPackage.PRIMITIVE_TYPE__N:
				return getN();
			case UniversityPackage.PRIMITIVE_TYPE__O:
				return getO();
			case UniversityPackage.PRIMITIVE_TYPE__P:
				return getP();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case UniversityPackage.PRIMITIVE_TYPE__A:
				setA((String)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__B:
				setB((Integer)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__C:
				setC((Integer)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__BIG_INT_LIST:
				getBigIntList().clear();
				getBigIntList().addAll((Collection<? extends Integer>)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__D:
				setD((Boolean)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__E:
				setE((Boolean)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__F:
				setF((Float)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__G:
				setG((Float)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__H:
				setH((Double)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__I:
				setI((Double)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__J:
				setJ((Byte)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__K:
				setK((Byte)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__L:
				setL((Short)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__M:
				setM((Short)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__N:
				setN((Long)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__O:
				setO((Long)newValue);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__P:
				setP((Character)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case UniversityPackage.PRIMITIVE_TYPE__A:
				setA(A_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__B:
				setB(B_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__C:
				setC(C_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__BIG_INT_LIST:
				getBigIntList().clear();
				return;
			case UniversityPackage.PRIMITIVE_TYPE__D:
				setD(D_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__E:
				setE(E_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__F:
				setF(F_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__G:
				setG(G_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__H:
				setH(H_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__I:
				setI(I_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__J:
				setJ(J_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__K:
				setK(K_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__L:
				setL(L_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__M:
				setM(M_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__N:
				setN(N_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__O:
				setO(O_EDEFAULT);
				return;
			case UniversityPackage.PRIMITIVE_TYPE__P:
				setP(P_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case UniversityPackage.PRIMITIVE_TYPE__A:
				return A_EDEFAULT == null ? a != null : !A_EDEFAULT.equals(a);
			case UniversityPackage.PRIMITIVE_TYPE__B:
				return b != B_EDEFAULT;
			case UniversityPackage.PRIMITIVE_TYPE__C:
				return C_EDEFAULT == null ? c != null : !C_EDEFAULT.equals(c);
			case UniversityPackage.PRIMITIVE_TYPE__BIG_INT_LIST:
				return bigIntList != null && !bigIntList.isEmpty();
			case UniversityPackage.PRIMITIVE_TYPE__D:
				return d != D_EDEFAULT;
			case UniversityPackage.PRIMITIVE_TYPE__E:
				return E_EDEFAULT == null ? e != null : !E_EDEFAULT.equals(e);
			case UniversityPackage.PRIMITIVE_TYPE__F:
				return f != F_EDEFAULT;
			case UniversityPackage.PRIMITIVE_TYPE__G:
				return G_EDEFAULT == null ? g != null : !G_EDEFAULT.equals(g);
			case UniversityPackage.PRIMITIVE_TYPE__H:
				return H_EDEFAULT == null ? h != null : !H_EDEFAULT.equals(h);
			case UniversityPackage.PRIMITIVE_TYPE__I:
				return i != I_EDEFAULT;
			case UniversityPackage.PRIMITIVE_TYPE__J:
				return j != J_EDEFAULT;
			case UniversityPackage.PRIMITIVE_TYPE__K:
				return K_EDEFAULT == null ? k != null : !K_EDEFAULT.equals(k);
			case UniversityPackage.PRIMITIVE_TYPE__L:
				return l != L_EDEFAULT;
			case UniversityPackage.PRIMITIVE_TYPE__M:
				return M_EDEFAULT == null ? m != null : !M_EDEFAULT.equals(m);
			case UniversityPackage.PRIMITIVE_TYPE__N:
				return n != N_EDEFAULT;
			case UniversityPackage.PRIMITIVE_TYPE__O:
				return O_EDEFAULT == null ? o != null : !O_EDEFAULT.equals(o);
			case UniversityPackage.PRIMITIVE_TYPE__P:
				return p != P_EDEFAULT;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (a: ");
		result.append(a);
		result.append(", b: ");
		result.append(b);
		result.append(", c: ");
		result.append(c);
		result.append(", bigIntList: ");
		result.append(bigIntList);
		result.append(", d: ");
		result.append(d);
		result.append(", e: ");
		result.append(e);
		result.append(", f: ");
		result.append(f);
		result.append(", g: ");
		result.append(g);
		result.append(", h: ");
		result.append(h);
		result.append(", i: ");
		result.append(i);
		result.append(", j: ");
		result.append(j);
		result.append(", k: ");
		result.append(k);
		result.append(", l: ");
		result.append(l);
		result.append(", m: ");
		result.append(m);
		result.append(", n: ");
		result.append(n);
		result.append(", o: ");
		result.append(o);
		result.append(", p: ");
		result.append(p);
		result.append(')');
		return result.toString();
	}

} //PrimitiveTypeImpl
