
/* First created by JCasGen Mon Oct 10 16:33:03 CEST 2016 */
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
import org.apache.uima.jcas.tcas.Annotation_Type;

/** 
 * Updated by JCasGen Tue Oct 11 15:27:10 CEST 2016
 * @generated */
public class posSequenceCount_Type extends Annotation_Type {
  /** @generated 
   * @return the generator for this type
   */
  @Override
  protected FSGenerator getFSGenerator() {return fsGenerator;}
  /** @generated */
  private final FSGenerator fsGenerator = 
    new FSGenerator() {
      public FeatureStructure createFS(int addr, CASImpl cas) {
  			 if (posSequenceCount_Type.this.useExistingInstance) {
  			   // Return eq fs instance if already created
  		     FeatureStructure fs = posSequenceCount_Type.this.jcas.getJfsFromCaddr(addr);
  		     if (null == fs) {
  		       fs = new posSequenceCount(addr, posSequenceCount_Type.this);
  			   posSequenceCount_Type.this.jcas.putJfsFromCaddr(addr, fs);
  			   return fs;
  		     }
  		     return fs;
        } else return new posSequenceCount(addr, posSequenceCount_Type.this);
  	  }
    };
  /** @generated */
  @SuppressWarnings ("hiding")
  public final static int typeIndexID = posSequenceCount.typeIndexID;
  /** @generated 
     @modifiable */
  @SuppressWarnings ("hiding")
  public final static boolean featOkTst = JCasRegistry.getFeatOkTst("com.iai.uima.jcas.tcas.posSequenceCount");
 
  /** @generated */
  final Feature casFeat_posSequenceCount;
  /** @generated */
  final int     casFeatCode_posSequenceCount;
  /** @generated
   * @param addr low level Feature Structure reference
   * @return the feature value 
   */ 
  public String getPosSequenceCount(int addr) {
        if (featOkTst && casFeat_posSequenceCount == null)
      jcas.throwFeatMissing("posSequenceCount", "com.iai.uima.jcas.tcas.posSequenceCount");
    return ll_cas.ll_getStringValue(addr, casFeatCode_posSequenceCount);
  }
  /** @generated
   * @param addr low level Feature Structure reference
   * @param v value to set 
   */    
  public void setPosSequenceCount(int addr, String v) {
        if (featOkTst && casFeat_posSequenceCount == null)
      jcas.throwFeatMissing("posSequenceCount", "com.iai.uima.jcas.tcas.posSequenceCount");
    ll_cas.ll_setStringValue(addr, casFeatCode_posSequenceCount, v);}
    
  



  /** initialize variables to correspond with Cas Type and Features
	 * @generated
	 * @param jcas JCas
	 * @param casType Type 
	 */
  public posSequenceCount_Type(JCas jcas, Type casType) {
    super(jcas, casType);
    casImpl.getFSClassRegistry().addGeneratorForType((TypeImpl)this.casType, getFSGenerator());

 
    casFeat_posSequenceCount = jcas.getRequiredFeatureDE(casType, "posSequenceCount", "uima.cas.String", featOkTst);
    casFeatCode_posSequenceCount  = (null == casFeat_posSequenceCount) ? JCas.INVALID_FEATURE_CODE : ((FeatureImpl)casFeat_posSequenceCount).getCode();

  }
}



    