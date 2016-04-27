
/* First created by JCasGen Wed Sep 23 15:28:31 CEST 2015 */
package com.iai.uima.jcas.tcas;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JCasRegistry;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.FSGenerator;
import org.apache.uima.cas.FeatureStructure;
import org.apache.uima.cas.impl.TypeImpl;
import org.apache.uima.cas.Type;
import org.apache.uima.cas.impl.FeatureImpl;
import org.apache.uima.cas.Feature;

/** 
 * Updated by JCasGen Mon Apr 25 14:01:52 CEST 2016
 * @generated */
public class KeyPhraseAnnotationReplaced_Type extends KeyPhraseAnnotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (KeyPhraseAnnotationReplaced_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = KeyPhraseAnnotationReplaced_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new KeyPhraseAnnotationReplaced(addr, KeyPhraseAnnotationReplaced_Type.this);
  			   KeyPhraseAnnotationReplaced_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new KeyPhraseAnnotationReplaced(addr, KeyPhraseAnnotationReplaced_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = KeyPhraseAnnotationReplaced.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
 
  /** @generated */
  final Feature casFeat_isAdjectiveReplacedWithNoun;
  /** @generated */
  final int     casFeatCode_isAdjectiveReplacedWithNoun;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getIsAdjectiveReplacedWithNoun(int addr) {
        if (featOkTst && casFeat_isAdjectiveReplacedWithNoun == null)
      jcas.throwFeatMissing("isAdjectiveReplacedWithNoun", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_isAdjectiveReplacedWithNoun);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setIsAdjectiveReplacedWithNoun(int addr, boolean v) {
        if (featOkTst && casFeat_isAdjectiveReplacedWithNoun == null)
      jcas.throwFeatMissing("isAdjectiveReplacedWithNoun", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_isAdjectiveReplacedWithNoun, v);}
    
  
 
  /** @generated */
  final Feature casFeat_replacee;
  /** @generated */
  final int     casFeatCode_replacee;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getReplacee(int addr) {
        if (featOkTst && casFeat_replacee == null)
      jcas.throwFeatMissing("replacee", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
    return ll_cas.ll_getStringValue(addr, casFeatCode_replacee);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setReplacee(int addr, String v) {
        if (featOkTst && casFeat_replacee == null)
      jcas.throwFeatMissing("replacee", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
    ll_cas.ll_setStringValue(addr, casFeatCode_replacee, v);}
    
  
 
  /** @generated */
  final Feature casFeat_isVerbReplacedWithNoun;
  /** @generated */
  final int     casFeatCode_isVerbReplacedWithNoun;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getIsVerbReplacedWithNoun(int addr) {
        if (featOkTst && casFeat_isVerbReplacedWithNoun == null)
      jcas.throwFeatMissing("isVerbReplacedWithNoun", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_isVerbReplacedWithNoun);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setIsVerbReplacedWithNoun(int addr, boolean v) {
        if (featOkTst && casFeat_isVerbReplacedWithNoun == null)
      jcas.throwFeatMissing("isVerbReplacedWithNoun", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationReplaced");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_isVerbReplacedWithNoun, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public KeyPhraseAnnotationReplaced_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_isAdjectiveReplacedWithNoun = jcas.getRequiredFeatureDE(casType, "isAdjectiveReplacedWithNoun", "uima.cas.Boolean", featOkTst);
    casFeatCode_isAdjectiveReplacedWithNoun  = (null == casFeat_isAdjectiveReplacedWithNoun) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_isAdjectiveReplacedWithNoun).getCode();

 
    casFeat_replacee = jcas.getRequiredFeatureDE(casType, "replacee", "uima.cas.String", featOkTst);
    casFeatCode_replacee  = (null == casFeat_replacee) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_replacee).getCode();

 
    casFeat_isVerbReplacedWithNoun = jcas.getRequiredFeatureDE(casType, "isVerbReplacedWithNoun", "uima.cas.Boolean", featOkTst);
    casFeatCode_isVerbReplacedWithNoun  = (null == casFeat_isVerbReplacedWithNoun) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_isVerbReplacedWithNoun).getCode();

  }
}



    