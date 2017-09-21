package com.btpn.sinaya.eform.connection;

import android.content.Context;

import com.btpn.sinaya.eform.connection.listener.MTFCustomChallengeListener;
import com.btpn.sinaya.eform.utils.MTFAES;
import com.btpn.sinaya.eform.utils.MTFConstants;
import com.sap.mobile.lib.configuration.Preferences;
import com.sap.mobile.lib.request.ConnectivityParameters;
import com.sap.mobile.lib.request.HttpChannelListeners;
import com.sap.mobile.lib.request.HttpsTrustManager;
import com.sap.mobile.lib.request.RequestManager;
import com.sap.mobile.lib.supportability.Logger;
import com.sap.smp.rest.ClientConnection;
import com.sap.smp.rest.SMPException;
import com.sap.smp.rest.UserManager;
import com.sybase.persistence.DataVault;
import com.sybase.persistence.PrivateDataVault;

/**
 * Created by vaniawidjaja on 2/7/17.
 */

public class MTFConnectionManager {
    public static String appId = "rfb.app.eform";
    public static String domain = "default";
    public static String host;
    public static String port;
    public static int portHttps;
    public static String certName;
    public static String requestType;
    public static String secConfig;
    public static String dataMTF;
    public static int dbVersion;
    ClientConnection clientConnection;
    private Context mContext;
    public static RequestManager requestManager;
    public UserManager userManager;
    private MTFCustomChallengeListener localCustomChallengeListener;
    public ConnectivityParameters param;
    protected DataVault dataVault;
    public static boolean useDataPur = true;

    static {
        configureCalvin();
    }

        /**
     *  Empty method to initialize value
     */
    public static void  init(){
//        configureProdNew();
    }

    private static void configureCalvin(){
        //useDataPur = false;
        MTFSMPUtilities.isHttpRequest = true;
        MTFSMPUtilities.requestType = requestType = "http://";
        MTFSMPUtilities.host = host = "192.168.0.107";
        MTFSMPUtilities.port = port = "8080";
        MTFSMPUtilities.portHttps = portHttps = Preferences.DEFAULT_HTTPS_PORT;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        dataMTF = "data_pur_2.mtf";
        /*MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100e149f7f5d57ac3047d7d72fe603e08c8ca88172b36d146e81410acb76523a710b7404f7859f1034b91f2a0d9712eb9de36dba4d9cbe4c539bfbe09f68bba6163ddfcf3aecc3dba2131fb0833f60c59ea6a6488c42e77b7cfb99271c55bedd9751479aeff120c9bbf4a15d3f62c2536fe40ba8aeee28a0de36a956cbba6036edddb7bbf3ffd78da95495393561f2bb8b1c47fad918386013b986ff8f5c2ef90380468ffcf22899299587a6826a9dca6247a79ddd3f8cf04aa729c1dfa2813e19bc48ebb4d9a1625426daa5a7acf60041365c222c6dbab0a4efde32ae98bbef6098cbd7adba3d3cc4de5bd44c32c48a7c2f92299f2394f132d4b1e98fc4766c4270203010001";*/
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100e149f7f5d57ac3047d7d72fe603e08c8ca88172b36d146e81410acb76523a710b7404f7859f1034b91f2a0d9712eb9de36dba4d9cbe4c539bfbe09f68bba6163ddfcf3aecc3dba2131fb0833f60c59ea6a6488c42e77b7cfb99271c55bedd9751479aeff120c9bbf4a15d3f62c2536fe40ba8aeee28a0de36a956cbba6036edddb7bbf3ffd78da95495393561f2bb8b1c47fad918386013b986ff8f5c2ef90380468ffcf22899299587a6826a9dca6247a79ddd3f8cf04aa729c1dfa2813e19bc48ebb4d9a1625426daa5a7acf60041365c222c6dbab0a4efde32ae98bbef6098cbd7adba3d3cc4de5bd44c32c48a7c2f92299f2394f132d4b1e98fc4766c4270203010001";
        MTFConstants.SSL_CHECK_SUM 	= "174653844";
        MTFConstants.BTPN_API_KEY_VALUE 	= "8428c326-ae66-47c7-9b06-1797ac513911";
        dbVersion = 1;
    }

    private static void configureNEWSIT(){
        //useDataPur = false;
        MTFSMPUtilities.isHttpRequest = false;
        if(MTFConstants.isUseGateway) {
            MTFSMPUtilities.requestType = requestType = "https://";
            MTFSMPUtilities.host = host = "appapisit02.dev.corp.btpn.co.id";
            MTFSMPUtilities.port = port = "9501";
        }else {
            MTFSMPUtilities.requestType = requestType = "http://";
            //MTFSMPUtilities.host = host = "192.168.43.58";
            //MTFSMPUtilities.host = host = "192.168.43.155";
            //MTFSMPUtilities.host = host = "10.157.205.112";
            //MTFSMPUtilities.host = host = "172.20.10.3";
            MTFSMPUtilities.host = host = "10.1.72.222";
            //MTFSMPUtilities.host = host = "10.157.205.112";
            //MTFSMPUtilities.host = host = "localhost";
            MTFSMPUtilities.port = port = "7090";
        }
        MTFSMPUtilities.portHttps = portHttps = Preferences.DEFAULT_HTTPS_PORT;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        dataMTF = "data_pur_2.mtf";
        /*MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100e149f7f5d57ac3047d7d72fe603e08c8ca88172b36d146e81410acb76523a710b7404f7859f1034b91f2a0d9712eb9de36dba4d9cbe4c539bfbe09f68bba6163ddfcf3aecc3dba2131fb0833f60c59ea6a6488c42e77b7cfb99271c55bedd9751479aeff120c9bbf4a15d3f62c2536fe40ba8aeee28a0de36a956cbba6036edddb7bbf3ffd78da95495393561f2bb8b1c47fad918386013b986ff8f5c2ef90380468ffcf22899299587a6826a9dca6247a79ddd3f8cf04aa729c1dfa2813e19bc48ebb4d9a1625426daa5a7acf60041365c222c6dbab0a4efde32ae98bbef6098cbd7adba3d3cc4de5bd44c32c48a7c2f92299f2394f132d4b1e98fc4766c4270203010001";*/
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100e149f7f5d57ac3047d7d72fe603e08c8ca88172b36d146e81410acb76523a710b7404f7859f1034b91f2a0d9712eb9de36dba4d9cbe4c539bfbe09f68bba6163ddfcf3aecc3dba2131fb0833f60c59ea6a6488c42e77b7cfb99271c55bedd9751479aeff120c9bbf4a15d3f62c2536fe40ba8aeee28a0de36a956cbba6036edddb7bbf3ffd78da95495393561f2bb8b1c47fad918386013b986ff8f5c2ef90380468ffcf22899299587a6826a9dca6247a79ddd3f8cf04aa729c1dfa2813e19bc48ebb4d9a1625426daa5a7acf60041365c222c6dbab0a4efde32ae98bbef6098cbd7adba3d3cc4de5bd44c32c48a7c2f92299f2394f132d4b1e98fc4766c4270203010001";
        MTFConstants.SSL_CHECK_SUM 	= "174653844";
        MTFConstants.BTPN_API_KEY_VALUE 	= "8428c326-ae66-47c7-9b06-1797ac513911";
        dbVersion = 1;
    }

    private static void configureNEWUATDemo(){
        //useDataPur = false;
        MTFSMPUtilities.isHttpRequest = false;
        MTFSMPUtilities.requestType = requestType = "https://";
        MTFSMPUtilities.host = host = "apidev.btpn.com";
        MTFSMPUtilities.port = port = "9502";
        MTFSMPUtilities.portHttps = portHttps = Preferences.DEFAULT_HTTPS_PORT;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        MTFSMPUtilities.isUAT = true;
        dataMTF = "data_pur.mtf";
        /*MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100e149f7f5d57ac3047d7d72fe603e08c8ca88172b36d146e81410acb76523a710b7404f7859f1034b91f2a0d9712eb9de36dba4d9cbe4c539bfbe09f68bba6163ddfcf3aecc3dba2131fb0833f60c59ea6a6488c42e77b7cfb99271c55bedd9751479aeff120c9bbf4a15d3f62c2536fe40ba8aeee28a0de36a956cbba6036edddb7bbf3ffd78da95495393561f2bb8b1c47fad918386013b986ff8f5c2ef90380468ffcf22899299587a6826a9dca6247a79ddd3f8cf04aa729c1dfa2813e19bc48ebb4d9a1625426daa5a7acf60041365c222c6dbab0a4efde32ae98bbef6098cbd7adba3d3cc4de5bd44c32c48a7c2f92299f2394f132d4b1e98fc4766c4270203010001";*/
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100c6257cad47b8400713313bcd4b3d7d0195ef5df9c334ad4731cbaec4d8170b47448294c8a15bc2099758c0942c390d1e9187fb544b377a79ed8e36767dc8421a6b48b19b93545c170e06986240eba4376daaad1cb669acb379d3849138653c2299f6bf6e544bd7771857641001853e4796a01bbb41446572a2a5158c3dac4033cfab93784817f7e93e20342966cd5ee02caebfae26bc42a1e05733dc98c8a1d4ece9cea2e6f705e3dd248870d0c271991e1d067aa513a9463cd47b015254df8212b44229a3b9dc850a8f99b89faefd6377cb8c075371b0c9d36a3735acd439326490097e0b6ecc2f3367376b12e59a057274985c9ddead79b262f15f5861ff370203010001";
        MTFConstants.SSL_CHECK_SUM 	= "174653844";
        MTFConstants.BTPN_API_KEY_VALUE 	= "546b8395-1a98-4e32-9a41-7bdaf4da773c";
        MTFConstants.LOAN_PACKAGE = "com.btpn.purna.eformloan.demo";
        dbVersion = 1;
    }

    private static void configureNEWUAT(){
        //useDataPur = false;
        MTFSMPUtilities.isHttpRequest = false;
        MTFSMPUtilities.requestType = requestType = "https://";
        MTFSMPUtilities.host = host = "apidev.btpn.com";
        MTFSMPUtilities.port = port = "9502";
        MTFSMPUtilities.portHttps = portHttps = Preferences.DEFAULT_HTTPS_PORT;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        MTFSMPUtilities.isUAT = true;
        dataMTF = "data_pur.mtf";
        /*MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100e149f7f5d57ac3047d7d72fe603e08c8ca88172b36d146e81410acb76523a710b7404f7859f1034b91f2a0d9712eb9de36dba4d9cbe4c539bfbe09f68bba6163ddfcf3aecc3dba2131fb0833f60c59ea6a6488c42e77b7cfb99271c55bedd9751479aeff120c9bbf4a15d3f62c2536fe40ba8aeee28a0de36a956cbba6036edddb7bbf3ffd78da95495393561f2bb8b1c47fad918386013b986ff8f5c2ef90380468ffcf22899299587a6826a9dca6247a79ddd3f8cf04aa729c1dfa2813e19bc48ebb4d9a1625426daa5a7acf60041365c222c6dbab0a4efde32ae98bbef6098cbd7adba3d3cc4de5bd44c32c48a7c2f92299f2394f132d4b1e98fc4766c4270203010001";*/
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100c6257cad47b8400713313bcd4b3d7d0195ef5df9c334ad4731cbaec4d8170b47448294c8a15bc2099758c0942c390d1e9187fb544b377a79ed8e36767dc8421a6b48b19b93545c170e06986240eba4376daaad1cb669acb379d3849138653c2299f6bf6e544bd7771857641001853e4796a01bbb41446572a2a5158c3dac4033cfab93784817f7e93e20342966cd5ee02caebfae26bc42a1e05733dc98c8a1d4ece9cea2e6f705e3dd248870d0c271991e1d067aa513a9463cd47b015254df8212b44229a3b9dc850a8f99b89faefd6377cb8c075371b0c9d36a3735acd439326490097e0b6ecc2f3367376b12e59a057274985c9ddead79b262f15f5861ff370203010001";
        MTFConstants.SSL_CHECK_SUM 	= "174653844";
        MTFConstants.BTPN_API_KEY_VALUE 	= "546b8395-1a98-4e32-9a41-7bdaf4da773c";
        dbVersion = 1;
    }

    private static void configureProdNew(){
        MTFSMPUtilities.isHttpRequest = false;
        MTFSMPUtilities.requestType = requestType = "https://";
        MTFSMPUtilities.host = host = "api.btpn.com";
        MTFSMPUtilities.port = port = "9502";
        MTFSMPUtilities.portHttps = portHttps = Preferences.DEFAULT_HTTPS_PORT;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        dataMTF = "data_pur.mtf";
        /*MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100e149f7f5d57ac3047d7d72fe603e08c8ca88172b36d146e81410acb76523a710b7404f7859f1034b91f2a0d9712eb9de36dba4d9cbe4c539bfbe09f68bba6163ddfcf3aecc3dba2131fb0833f60c59ea6a6488c42e77b7cfb99271c55bedd9751479aeff120c9bbf4a15d3f62c2536fe40ba8aeee28a0de36a956cbba6036edddb7bbf3ffd78da95495393561f2bb8b1c47fad918386013b986ff8f5c2ef90380468ffcf22899299587a6826a9dca6247a79ddd3f8cf04aa729c1dfa2813e19bc48ebb4d9a1625426daa5a7acf60041365c222c6dbab0a4efde32ae98bbef6098cbd7adba3d3cc4de5bd44c32c48a7c2f92299f2394f132d4b1e98fc4766c4270203010001";*/
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a02820101009d034f379a1cce289ecac4e62135e26b6fa193ff15c7a1c682bf63fa14fa1522f0f3573f625c42c5f0dfedeb4bd95d19c238d8b7a1cb53cc72bf5f184bff12e37c703203034f03d4190140fefaa441a55698a41190fabf3306fa3a1a8a34bc76d94bf62d977c319cf0bce3d2bba2fbcbd8971d31b13b25524e3443a60d00bf6b345bb89d410248e18f0174251b9e4b5ad39b441203bba7549bfdcb9f758b2e439678f29920c29733257437d659de6ea1f2d186aa9489ca8bcc49cd4cf423d6b2279d39e16355aff4e87849bcbd6a69e1722cd0f7c8aff11f2058c86872f79e82a4cbd3005eafdff36e111d9e916d0aec031a3892f225b1de2bc9e821dcda71650203010001";
        MTFConstants.SSL_CHECK_SUM 	= "174653844";
        MTFConstants.BTPN_API_KEY_VALUE 	= "8428c326-ae66-47c7-9b06-1797ac513911";
        dbVersion = 1;
    }

    private static void configureUATPT(){
        //useDataPur = false;
        MTFSMPUtilities.isHttpRequest = true;
        MTFSMPUtilities.requestType = requestType = "http://";
        MTFSMPUtilities.host = host = "apim-ext.dev.corp.btpn.co.id";
        MTFSMPUtilities.port = port = "8084";
        MTFSMPUtilities.portHttps = portHttps = Preferences.DEFAULT_HTTPS_PORT;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        dataMTF = "data_pur.mtf";
        /*MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100e149f7f5d57ac3047d7d72fe603e08c8ca88172b36d146e81410acb76523a710b7404f7859f1034b91f2a0d9712eb9de36dba4d9cbe4c539bfbe09f68bba6163ddfcf3aecc3dba2131fb0833f60c59ea6a6488c42e77b7cfb99271c55bedd9751479aeff120c9bbf4a15d3f62c2536fe40ba8aeee28a0de36a956cbba6036edddb7bbf3ffd78da95495393561f2bb8b1c47fad918386013b986ff8f5c2ef90380468ffcf22899299587a6826a9dca6247a79ddd3f8cf04aa729c1dfa2813e19bc48ebb4d9a1625426daa5a7acf60041365c222c6dbab0a4efde32ae98bbef6098cbd7adba3d3cc4de5bd44c32c48a7c2f92299f2394f132d4b1e98fc4766c4270203010001";*/
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100c9aaf1ed3f66c238d02892eb264a0f6b3bf0961e63b9f3dceac7df8b68b764b9ecfc55848f0732465fc75afdd227a00d05a4c8864564a2dbb7a03822cd6d17cafa294299e9aeb02b56e85dcf20e9fc5c0a028fd591bbf8601d25d77754fac326ff2a1684287d2ef189dcb8b037a02dc5f4cdbcd5c168dcea50f311b628b9aa07e53916cce9ed1a6e2a8f0774e38e15f37612db3f2469e7aee883c3ebe1fef3be73dd7ba27a0033ef156513f7e21962547d8059aa0c9bcd597d2b9f19810a7e087eaba3d93f4552cc6de1cefe709ae2d4ca49e87f57c109dc686f6e7b9b83f34957d9532149a16ccd82e4a267801bf3f239c6df063c8beaceb411aabfd657bf230203010001";
        MTFConstants.SSL_CHECK_SUM 	= "174653844";
        MTFConstants.BTPN_API_KEY_VALUE 	= "8428c326-ae66-47c7-9b06-1797ac513911";
        dbVersion = 1;
    }

    private static void configureNewProd(){
        MTFSMPUtilities.isHttpRequest = false;
        MTFSMPUtilities.requestType = requestType = "https://";
        MTFSMPUtilities.host = host = "smp.btpn.com";
        MTFSMPUtilities.port = port = null;
        MTFSMPUtilities.portHttps = portHttps = Preferences.DEFAULT_HTTPS_PORT;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        dataMTF = "data.mtf";
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100bad2c8392d14e511582ea29368f21a798a9058b012c3d566b87aa2b6057938e65cef981cd4ba23aa9dfac36d609e067e57c2a30bace3e3ce90f0f3e09f0fe4070c72cbb46d735d74fc7cbfa5e07d148847b03ed6a7b54cefa25e5235b1fe7abbc1a848eeb69822cf6cd0b2f8346a3f79ad6145887ce789c10f35b000d7506c8c47de630c9242578c1e6470ee4c901cc3a32b4df7abedf703b62e5be8f05635b830c4a3dc3d977eeeaaa290637509e320ff003f7d82fc00b04cfac9fb777895861d3ad06b73c38ff01b655157e7a681549149fdad5d630571fbde4865668584e4ba1f19608e8df714c92e4faefa2eeda21e1ee6d48882a9cd2557e213f90f00270203010001";
        MTFConstants.SSL_CHECK_SUM 	= "174653844";
        dbVersion = 1;
    }

    private static void configureProd(){
        MTFSMPUtilities.isHttpRequest = false;
        MTFSMPUtilities.requestType = requestType = "https://";
        MTFSMPUtilities.host = host = "www.btpn.com/smp";
        MTFSMPUtilities.port = port = null;
        MTFSMPUtilities.portHttps = portHttps = Preferences.DEFAULT_HTTPS_PORT;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        certName = "www.btpn.com";
        dataMTF = "data.mtf";
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100dd4c97a417aaf2707312844a66185e4611b66fdb05bd8874aa20f92321ded714ad79fd16dc272da4445f0e19bcdcd4cca309908d938507ebe5fa15a77cb028f463914bb4bf53ae8b4ff0536d4c04d483351ef1240dee803e9a10004b10ddb2d1eeda247ed412d4f5530386b7317a37ac6a3415b90e69e97447000ae58652596e8ca696d008b25a79655ee3b10fec9eba0a7fd24d822754a900c25a3b8825e7ef90e7b49041b019dd65c30678b82193c4ee1e6d7b9808003b47773f4f738a2ddea9a47f1a355ff88f81d2fcc2b76a0ad8b5a2c31e9f18a9322adf49760a7b63a5d481ae85d6dc73370fed2a81a121124e0d79f02707d15e9b6ce20974bb03669b0203010001";
        MTFConstants.SSL_CHECK_SUM 	= "478508962";
        dbVersion = 1;
    }

    private static void configureUAT(){
        MTFSMPUtilities.isHttpRequest = false;
        MTFSMPUtilities.requestType = requestType = "https://";
        MTFSMPUtilities.host = host = "meap-uat.dev.corp.btpn.co.id/smp";
        MTFSMPUtilities.port = port = null;
        MTFSMPUtilities.portHttps = portHttps = Preferences.DEFAULT_HTTPS_PORT;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        certName = "uat";
        dataMTF = "data.mtf";
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100ece149d838ab083f0eaad2364573da827482d1dac1dc8f352ab6ec25d08a238dd003bad98219bfaf8079ce6139512cf5a1d870e2630c4e37e0598530e6a98d6aa92e4910a7bc77f80eeba9ee75ef23c34152ba591c810d86f88d3daf72643376f4060f21bc97818ce97326e5d9206eb34f144ffc8e8c6983348bae27ad6c2a744a7624496ae9dfa29b99e6eca95fa84b5da7de6deb8ecaa4f979895d5e03dadfd38511e800aad2b248ec0be5e6336467503a587aac972454b5c38225833e35de1a041fade8e3fb7e1260d97d0740186457ecfee851eaee2e10afbb988247c16a7fca825fd584958fb9f5263503aabbc8c2ae6e8f270bd22329e05dc3ec7f80830203010001";
        MTFConstants.SSL_CHECK_SUM 	= "3723886597";
        dbVersion = 987;
    }

    private static void configureUAT03(){
        MTFSMPUtilities.isHttpRequest = true;
        MTFSMPUtilities.requestType = requestType = "http://";
        MTFSMPUtilities.host = host = "appsmpuat03.dev.corp.btpn.co.id";
        MTFSMPUtilities.port = port = "8080";
        MTFSMPUtilities.portHttps = portHttps = Preferences.DEFAULT_HTTPS_PORT;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        certName = "uat";
        dataMTF = "data.mtf";
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100c2fed4aa185eefbaabc2ed8f56d82cd084c6c10f53deb9db76239e4cef44ffd1ec4b957616149f9dc4fa80fa11c079932c91650ca7de420df576cce3419c1049611e8ae206d63bad2f1202241f30d40ae081916359cec113cb452b77e632f50fae960cf80fd27b51be044f0e8a4f105fc21fc256496b9d01d6c91e98759e098ec79ffeb557afbdc2ab14d94347a88b27f4d16fa0ce356a81832e6187235172d03d7bfd344cacac22cdc636f518ad5aff53eca4ec8e087850c1eda1aced260904f2b97aedf508cbf675ed9b4704687767d165df409f9bf21cab567f8af16b5a2d6d2d9f30ce5eeb1e362a496ae8c54d9f3f8980c9044e72af3b155c19380da2270203010001";
        MTFConstants.SSL_CHECK_SUM 	= "3723886597";
        dbVersion = 987;
    }

    private static void configureSMPSIT(){
        MTFSMPUtilities.isHttpRequest = true;
        MTFSMPUtilities.requestType = requestType = "http://";
        MTFSMPUtilities.host = host = "appsmpsit02.dev.corp.btpn.co.id";
        MTFSMPUtilities.port = port = "8080";
        MTFSMPUtilities.portHttps = portHttps = 8080;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        certName = "sit";
        dataMTF = "data.mtf";
        MTFConstants.SSL_PUBLIC_KEY = "30820222300d06092a864886f70d01010105000382020f003082020a0282020100a6f270320ce458d987b681ef9a294b6e50719e20b820bd4cdc844af3dd7744cb7bb3580f0f71afda9090c2cf384f59bf5aa523930e9933573d3ec7101552f4e60269815e5b0dd035ba56deb9737c6a44e5a0f665e5c063d7f16887d0b671baee33aceb15ee75a908e2c763546a886d92b40cd7d89cd03fc6d2058b8ae91ff3ff720ef1aee4cc5b57dbae4c2ed90163a093f875a0c47ac8fdcc876116871f6e8dcb19b68ec0eed5dfe5b5a7fa1fb17102f4e571f6a1810c297ddaf16d49b3bad8006e8c7e0d49ed1a3c0cc59ab0146e85fd42e7a501aa9456401eaf55f7b23428cabcd727b3076d2140c452073476371726025164cc91de6cbf13c605d6ff65b7753e7906e314479211e0d2ebb41730a9c192364c4aae4c8149cffac02c0e1280f22f2cfc978c3a4b6489fe96df5ecb089d78ce11ee65411d08898b05cbbb718f8312b6dee5715d909ad4f6d1aa8c2a863a8157025e463bcd5eccf2d000db5d4557f34dd123c5a76c9efa55df0de8bfe2a619703570d5dd3d60c45bc6ab52c24e27e594ed39a8d147e6d684351042cadc71b4ed7a5ae0d001447a2cb6d9100d77f084bc6a318cf5b7bba2bf1d9fa56743b944ec591e5e34fa3a68014b2e145f1bf45772a1bb4b30fe3099e1804fcf3f2960371b9537e192862d05e531f18a180243b67e9b8d0a719b94f60afd07f5d4245969d113dfb56971b92e8e140b75990f0203010001";
        MTFConstants.SSL_CHECK_SUM = "2020055936";
        dbVersion = 206;
    }

    private static void configureSIT(){
        MTFSMPUtilities.isHttpRequest = false;
        MTFSMPUtilities.requestType = requestType = "https://";
        MTFSMPUtilities.host = host = "meap-sit.dev.corp.btpn.co.id";
        MTFSMPUtilities.port = port = null;
        MTFSMPUtilities.portHttps = portHttps = Preferences.DEFAULT_HTTPS_PORT;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        certName = "sit";
        dataMTF = "data.mtf";
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100a2cbcab1dadcf68fbab2b96eb660440b9fda83706df46d36b0a611519c9ae4d9ceb99a98651ccb016ff445de3a5aadd82659322e12175bf55c5c7366115afd1876a45cf45368947b6fa6164bd837a9134079ec5fd038940117cbb1a174a62e2a911b716517cda528cca6c1478025eba2f87a9904cdeefd13da0dca94a28f809aacded9501bb6f2e660a2dacee2cd46afb43f7ea2a3e155e304661724cdba43014fe1a6ce10f4c2e607c4f93dcf235174a5964821d3a826aec2bd2c265b4bf03291bd576a6469b1a7eb159a6d7d1101d6779bd4ee8b1f904ef4679e8867761fe1bcf40710e65d33c0bd943616eea0746d949ac666dd524d971bfff5e297ef5a650203010001";
        MTFConstants.SSL_CHECK_SUM 	= "3979355959";
        dbVersion = 206;
    }

    private static void configureSIT01(){
        MTFSMPUtilities.isHttpRequest = true;
        MTFSMPUtilities.requestType = requestType = "http://";
        MTFSMPUtilities.host = host = "apptbodev01.dev.corp.btpn.co.id";
        MTFSMPUtilities.port = port = "8080";
        MTFSMPUtilities.portHttps = portHttps = 8081;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        certName = "sit";
        dataMTF = "data.mtf";
        //MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a02820101008639edcbfc90557ee7b9906365e6889a370e43e23d8d1bba3d65a72ba494533cad34cb4042a42f406ba2180fda01a07bde69b59c1f92b33d843714a7d64a079b428c46c952c4766f8e971e2a53636ba1593a6058cb46e8c1d97ad3f176d5d50ad2ec3b4d0ff5a09cd2f26277e4c8da19c1ab2beb3551250fd3fca21bb557bacdf0a9ad971c687be8d88271bda7e172529b690aef17062f6c1b2ef7cf8397cd0314c0291aa59bfd5aaea11efdf69aa5a40097e7620bc24cdc20d1d44e1aa56c544d35d7ecc433471729a0b21d0959c53b6ddf4af592249ff53ca150373f4d1d111a4f6a88f1ab575f6e08f58f0088f0186b4f1632e72215b853ee33c1cea81a2b0203010001";
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100dd4c97a417aaf2707312844a66185e4611b66fdb05bd8874aa20f92321ded714ad79fd16dc272da4445f0e19bcdcd4cca309908d938507ebe5fa15a77cb028f463914bb4bf53ae8b4ff0536d4c04d483351ef1240dee803e9a10004b10ddb2d1eeda247ed412d4f5530386b7317a37ac6a3415b90e69e97447000ae58652596e8ca696d008b25a79655ee3b10fec9eba0a7fd24d822754a900c25a3b8825e7ef90e7b49041b019dd65c30678b82193c4ee1e6d7b9808003b47773f4f738a2ddea9a47f1a355ff88f81d2fcc2b76a0ad8b5a2c31e9f18a9322adf49760a7b63a5d481ae85d6dc73370fed2a81a121124e0d79f02707d15e9b6ce20974bb03669b0203010001";
        MTFConstants.SSL_CHECK_SUM 	= "3979355959";
        dbVersion = 206;
    }

    private static void configureSIT03(){
        MTFSMPUtilities.isHttpRequest = false;
        MTFSMPUtilities.requestType = requestType = "https://";
        MTFSMPUtilities.host = host = "apptbodev03.dev.corp.btpn.co.id";
        MTFSMPUtilities.port = port = null;
        MTFSMPUtilities.portHttps = portHttps = 8081;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        certName = "sit";
        dataMTF = "data.mtf";
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a02820101008639edcbfc90557ee7b9906365e6889a370e43e23d8d1bba3d65a72ba494533cad34cb4042a42f406ba2180fda01a07bde69b59c1f92b33d843714a7d64a079b428c46c952c4766f8e971e2a53636ba1593a6058cb46e8c1d97ad3f176d5d50ad2ec3b4d0ff5a09cd2f26277e4c8da19c1ab2beb3551250fd3fca21bb557bacdf0a9ad971c687be8d88271bda7e172529b690aef17062f6c1b2ef7cf8397cd0314c0291aa59bfd5aaea11efdf69aa5a40097e7620bc24cdc20d1d44e1aa56c544d35d7ecc433471729a0b21d0959c53b6ddf4af592249ff53ca150373f4d1d111a4f6a88f1ab575f6e08f58f0088f0186b4f1632e72215b853ee33c1cea81a2b0203010001";
        MTFConstants.SSL_CHECK_SUM 	= "3979355959";
        dbVersion = 206;
    }

    private static void configureSIT02(){
        MTFSMPUtilities.isHttpRequest = false;
        MTFSMPUtilities.requestType = requestType = "https://";
        MTFSMPUtilities.host = host = "meap-sit02.dev.corp.btpn.co.id/smp";
        MTFSMPUtilities.port = port = null;
        MTFSMPUtilities.portHttps = portHttps = Preferences.DEFAULT_HTTPS_PORT;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        dataMTF = "data.mtf";
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100b035def40a7fe4b4f54d8b48af43abdbb6b320bcf8cc3b083ed5e6a9c3825e4b0d661ee4f2016a5dda9e7a1e98063bc0a2ef11d9db78bc6d010a15c5235d6dd3c9f288087e9d39bcaa312ce91a50e09b7c00daa24090a532465920326d5da1e2ba2be040a3081fc1f1dcf603480de5c44d46c1ce491cc66a867326fd97c584668f3e7f09748acb7ac43cbfa8f1b8a5ea3272b3d38e9fba54dbe731a4c3ba1a76e16856c843a4aaa813c86f98ee96ea8dcceb2d2effe436948dec4bbd5b21faf6035efd350802a5ab876b0827e686a0460dcd0169934a83cf4dea70c0b71385aa27b6289722a53ce0da4c53c9747a4e761524f905083333ad3956867fabb0189d0203010001";
        MTFConstants.SSL_CHECK_SUM 	= "2611142844";
        dbVersion = 206;
    }

    private static void configureDEVHTTPS(){
        MTFSMPUtilities.isHttpRequest = false;
        MTFSMPUtilities.requestType = requestType = "https://";
        MTFSMPUtilities.host = host = "apptbodev02.dev.corp.btpn.co.id";
        MTFSMPUtilities.port = port = "8081";
        MTFSMPUtilities.portHttps = portHttps = 8081;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        certName = "apptbodev02";
        dataMTF = "data.mtf";
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100dd4c97a417aaf2707312844a66185e4611b66fdb05bd8874aa20f92321ded714ad79fd16dc272da4445f0e19bcdcd4cca309908d938507ebe5fa15a77cb028f463914bb4bf53ae8b4ff0536d4c04d483351ef1240dee803e9a10004b10ddb2d1eeda247ed412d4f5530386b7317a37ac6a3415b90e69e97447000ae58652596e8ca696d008b25a79655ee3b10fec9eba0a7fd24d822754a900c25a3b8825e7ef90e7b49041b019dd65c30678b82193c4ee1e6d7b9808003b47773f4f738a2ddea9a47f1a355ff88f81d2fcc2b76a0ad8b5a2c31e9f18a9322adf49760a7b63a5d481ae85d6dc73370fed2a81a121124e0d79f02707d15e9b6ce20974bb03669b0203010001";
        MTFConstants.SSL_CHECK_SUM 	= "3979355959";
        dbVersion = 206;
    }

    private static void configureDEV(){
        MTFSMPUtilities.isHttpRequest = true;
        MTFSMPUtilities.requestType = requestType = "http://";
        MTFSMPUtilities.host = host = "apptbodev02.dev.corp.btpn.co.id";
        MTFSMPUtilities.port = port = "8080";
        MTFSMPUtilities.portHttps = portHttps = 8080;
        MTFSMPUtilities.secConfig = secConfig = "EFORM-SDB";
        certName = "apptbodev02";
        dataMTF = "data.mtf";
        MTFConstants.SSL_PUBLIC_KEY = "30820122300d06092a864886f70d01010105000382010f003082010a0282010100dd4c97a417aaf2707312844a66185e4611b66fdb05bd8874aa20f92321ded714ad79fd16dc272da4445f0e19bcdcd4cca309908d938507ebe5fa15a77cb028f463914bb4bf53ae8b4ff0536d4c04d483351ef1240dee803e9a10004b10ddb2d1eeda247ed412d4f5530386b7317a37ac6a3415b90e69e97447000ae58652596e8ca696d008b25a79655ee3b10fec9eba0a7fd24d822754a900c25a3b8825e7ef90e7b49041b019dd65c30678b82193c4ee1e6d7b9808003b47773f4f738a2ddea9a47f1a355ff88f81d2fcc2b76a0ad8b5a2c31e9f18a9322adf49760a7b63a5d481ae85d6dc73370fed2a81a121124e0d79f02707d15e9b6ce20974bb03669b0203010001";
        MTFConstants.SSL_CHECK_SUM 	= "3979355959";
        dbVersion = 206;
    }

    public MTFConnectionManager(Context paramContext) {
        this.mContext = paramContext;
        PrivateDataVault.init(mContext);
        initialize();
    }

    private void initialize() {
        if (MTFSMPUtilities.username.equals("") || MTFSMPUtilities.password.equals("")) {
            if (PrivateDataVault.vaultExists(MTFSMPUtilities.dataVaultName)) {
                DataVault dataVault = PrivateDataVault.getVault(MTFSMPUtilities.dataVaultName);
                dataVault.unlock(MTFSMPUtilities.appPasscode, MTFSMPUtilities.appPasscode + MTFSMPUtilities.appSaltcode);

                MTFSMPUtilities.username = dataVault.getString("username");
                MTFSMPUtilities.password = dataVault.getString("password");
            }
        }

        Logger localLogger = new Logger();
        Preferences localPreferences = new Preferences(this.mContext,localLogger);
        param = new ConnectivityParameters();
//        String username= "";
//        String password = "";
//        try {
//            MTFAES.generateKey();
//            username = MTFAES.encryptString(MTFSMPUtilities.username);
//            password = MTFAES.encryptString(MTFSMPUtilities.password);
//        } catch (Exception e) {
//
//        }
        param.setUserName(MTFSMPUtilities.username);
        param.setUserPassword(MTFSMPUtilities.password);
        param.setBaseUrl(requestType + host);
        requestManager = new RequestManager(localLogger, localPreferences,param, 1);
        localCustomChallengeListener = new MTFCustomChallengeListener();
        requestManager.setMutualSSLChallengeListener(new HttpChannelListeners.IMutualSSLChallengeListener() {
            public HttpsTrustManager.HttpsClientCertInfo getClientCertificate() {
                return null;
            }
        });
        requestManager.setSSLChallengeListener(localCustomChallengeListener);
        this.clientConnection = new ClientConnection(this.mContext, appId, domain, secConfig, requestManager);
        this.clientConnection.setConnectionProfile(requestType + host);
        this.userManager = new UserManager(this.clientConnection);
    }

    public void onBoard() {
        try {
            if(userManager.getApplicationConnectionId().equals("")){
                userManager.registerUser(true);
            }else{
                userManager.registerUser(false);
            }

            MTFSMPUtilities.appConnId = userManager.getApplicationConnectionId();
            if(!userManager.getApplicationConnectionId().equals("")){
                PrivateDataVault.init(mContext);

                if(PrivateDataVault.vaultExists(MTFSMPUtilities.getDataValultName())){
                    dataVault = PrivateDataVault.getVault(MTFSMPUtilities.getDataValultName());
                    dataVault.unlock(MTFSMPUtilities.appPasscode, MTFSMPUtilities.appPasscode+ MTFSMPUtilities.appSaltcode);
                }else{
                    dataVault = PrivateDataVault.createVault(MTFSMPUtilities.getDataValultName(), MTFSMPUtilities.appPasscode, MTFSMPUtilities.appPasscode+ MTFSMPUtilities.appSaltcode);
                }
                if(MTFSMPUtilities.appConnId.equals("")){
                    dataVault.setString("appCid", MTFSMPUtilities.userManager.getApplicationConnectionId());
                    MTFSMPUtilities.appConnId = MTFSMPUtilities.userManager.getApplicationConnectionId();
                }else{
                    dataVault.setString("appCid", MTFSMPUtilities.appConnId);
                }
                dataVault.setString("username", MTFSMPUtilities.username);
                dataVault.setString("password", MTFSMPUtilities.password);
                dataVault.setString("host", MTFSMPUtilities.host);
                dataVault.setString("port", MTFSMPUtilities.port);
                dataVault.setString("isHttp", MTFSMPUtilities.isHttpRequest.toString());
                dataVault.lock();
            }
        } catch (SMPException localSMPException) {
            localSMPException.printStackTrace();
            //return;
        }
    }
}