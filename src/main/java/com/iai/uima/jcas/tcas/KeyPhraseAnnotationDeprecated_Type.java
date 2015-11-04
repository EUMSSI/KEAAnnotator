
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
 * Updated by JCasGen Wed Oct 28 11:51:42 CET 2015
 * @generated */
public class KeyPhraseAnnotationDeprecated_Type extends KeyPhraseAnnotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (KeyPhraseAnnotationDeprecated_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = KeyPhraseAnnotationDeprecated_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new KeyPhraseAnnotationDeprecated(addr, KeyPhraseAnnotationDeprecated_Type.this);
  			   KeyPhraseAnnotationDeprecated_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new KeyPhraseAnnotationDeprecated(addr, KeyPhraseAnnotationDeprecated_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = KeyPhraseAnnotationDeprecated.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
 
  /** @generated */
  final Feature casFeat_containsStopword;
  /** @generated */
  final int     casFeatCode_containsStopword;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getContainsStopword(int addr) {
        if (featOkTst && casFeat_containsStopword == null)
      jcas.throwFeatMissing("containsStopword", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_containsStopword);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setContainsStopword(int addr, boolean v) {
        if (featOkTst && casFeat_containsStopword == null)
      jcas.throwFeatMissing("containsStopword", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_containsStopword, v);}
    
  
 
  /** @generated */
  final Feature casFeat_endsWithAdjective;
  /** @generated */
  final int     casFeatCode_endsWithAdjective;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getEndsWithAdjective(int addr) {
        if (featOkTst && casFeat_endsWithAdjective == null)
      jcas.throwFeatMissing("endsWithAdjective", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_endsWithAdjective);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setEndsWithAdjective(int addr, boolean v) {
        if (featOkTst && casFeat_endsWithAdjective == null)
      jcas.throwFeatMissing("endsWithAdjective", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_endsWithAdjective, v);}
    
  
 
  /** @generated */
  final Feature casFeat_containsAdverb;
  /** @generated */
  final int     casFeatCode_containsAdverb;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getContainsAdverb(int addr) {
        if (featOkTst && casFeat_containsAdverb == null)
      jcas.throwFeatMissing("containsAdverb", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_containsAdverb);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setContainsAdverb(int addr, boolean v) {
        if (featOkTst && casFeat_containsAdverb == null)
      jcas.throwFeatMissing("containsAdverb", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_containsAdverb, v);}
    
  
 
  /** @generated */
  final Feature casFeat_containsFiniteVerb;
  /** @generated */
  final int     casFeatCode_containsFiniteVerb;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getContainsFiniteVerb(int addr) {
        if (featOkTst && casFeat_containsFiniteVerb == null)
      jcas.throwFeatMissing("containsFiniteVerb", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_containsFiniteVerb);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setContainsFiniteVerb(int addr, boolean v) {
        if (featOkTst && casFeat_containsFiniteVerb == null)
      jcas.throwFeatMissing("containsFiniteVerb", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_containsFiniteVerb, v);}
    
  
 
  /** @generated */
  final Feature casFeat_isContainedInLongerKeyPhrase;
  /** @generated */
  final int     casFeatCode_isContainedInLongerKeyPhrase;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getIsContainedInLongerKeyPhrase(int addr) {
        if (featOkTst && casFeat_isContainedInLongerKeyPhrase == null)
      jcas.throwFeatMissing("isContainedInLongerKeyPhrase", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_isContainedInLongerKeyPhrase);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setIsContainedInLongerKeyPhrase(int addr, boolean v) {
        if (featOkTst && casFeat_isContainedInLongerKeyPhrase == null)
      jcas.throwFeatMissing("isContainedInLongerKeyPhrase", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_isContainedInLongerKeyPhrase, v);}
    
  
 
  /** @generated */
  final Feature casFeat_isContainedInDeprecatedKeyPhrase;
  /** @generated */
  final int     casFeatCode_isContainedInDeprecatedKeyPhrase;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public boolean getIsContainedInDeprecatedKeyPhrase(int addr) {
        if (featOkTst && casFeat_isContainedInDeprecatedKeyPhrase == null)
      jcas.throwFeatMissing("isContainedInDeprecatedKeyPhrase", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    return ll_cas.ll_getBooleanValue(addr, casFeatCode_isContainedInDeprecatedKeyPhrase);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setIsContainedInDeprecatedKeyPhrase(int addr, boolean v) {
        if (featOkTst && casFeat_isContainedInDeprecatedKeyPhrase == null)
      jcas.throwFeatMissing("isContainedInDeprecatedKeyPhrase", "com.iai.uima.jcas.tcas.KeyPhraseAnnotationDeprecated");
    ll_cas.ll_setBooleanValue(addr, casFeatCode_isContainedInDeprecatedKeyPhrase, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public KeyPhraseAnnotationDeprecated_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_containsStopword = jcas.getRequiredFeatureDE(casType, "containsStopword", "uima.cas.Boolean", featOkTst);
    casFeatCode_containsStopword  = (null == casFeat_containsStopword) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_containsStopword).getCode();

 
    casFeat_endsWithAdjective = jcas.getRequiredFeatureDE(casType, "endsWithAdjective", "uima.cas.Boolean", featOkTst);
    casFeatCode_endsWithAdjective  = (null == casFeat_endsWithAdjective) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_endsWithAdjective).getCode();

 
    casFeat_containsAdverb = jcas.getRequiredFeatureDE(casType, "containsAdverb", "uima.cas.Boolean", featOkTst);
    casFeatCode_containsAdverb  = (null == casFeat_containsAdverb) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_containsAdverb).getCode();

 
    casFeat_containsFiniteVerb = jcas.getRequiredFeatureDE(casType, "containsFiniteVerb", "uima.cas.Boolean", featOkTst);
    casFeatCode_containsFiniteVerb  = (null == casFeat_containsFiniteVerb) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_containsFiniteVerb).getCode();

 
    casFeat_isContainedInLongerKeyPhrase = jcas.getRequiredFeatureDE(casType, "isContainedInLongerKeyPhrase", "uima.cas.Boolean", featOkTst);
    casFeatCode_isContainedInLongerKeyPhrase  = (null == casFeat_isContainedInLongerKeyPhrase) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_isContainedInLongerKeyPhrase).getCode();

 
    casFeat_isContainedInDeprecatedKeyPhrase = jcas.getRequiredFeatureDE(casType, "isContainedInDeprecatedKeyPhrase", "uima.cas.Boolean", featOkTst);
    casFeatCode_isContainedInDeprecatedKeyPhrase  = (null == casFeat_isContainedInDeprecatedKeyPhrase) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_isContainedInDeprecatedKeyPhrase).getCode();

  }
}



    