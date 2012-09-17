package ch.unibas.medizin.osce.server;

public class OsMaFilePathConstant {

	// Module 9 Start

	public static String DOWNLOAD_DIR_PATH = "Download";

	public static String FILENAME = "StandardizedPatientList.csv";
	public static String FILE_NAME_PDF_FORMAT = "StandardizedPatientDetails.pdf";
	public static String ROLE_FILE_NAME_PDF_FORMAT = "StandardizedRoleDetails.pdf";

	public static String PATIENT_FILE_NAME_PDF_FORMAT = "StandardizedPatient.pdf";
	public static String STUDENT_FILE_NAME_PDF_FORMAT = "Student.pdf";
	public static String EXAMINER_FILE_NAME_PDF_FORMAT = "Examiner.pdf";
	public static String INVITATION_FILE_NAME_PDF_FORMAT = "Invitation.pdf";

	public static String TXT_EXTENTION = ".txt";

	public static String DEFAULT_SP_MAIL_TEMPLATE_PATH = "mail\\mailTemplate_SP";
	public static String DEFAULT_EXAMINER_MAIL_TEMPLATE_PATH = "mail\\mailTemplate_Ex";

	public static String DEFAULT_SP_EMAIL_TEMPLATE_PATH = "email\\emailTemplate_SP";
	public static String DEFAULT_EXAMINER_EMAIL_TEMPLATE_PATH = "email\\emailTemplate_Ex";

	public static String DEFAULT_MAIL_TEMPLATE = "osMaEntry/gwt/unibas/templates/defaultTemplate.txt";

	public static String TEMPLATE_PATH = "osMaEntry/gwt/unibas/templates/";
	public static String DEFAULT_TEMPLATE_PATH = "defaultTemplate";
	public static String UPDATED_TEMPLATE_PATH = "updatedTemplate";

	public static String UPDATED_TEMPLATE_EXAMINER = "UpdatedTemplateExaminer";
	public static String UPDATED_TEMPLATE_STUDENT = "UpdatedTemplateStudent";
	public static String UPDATED_TEMPLATE_SP = "UpdatedTemplateSP";

	public static String DEFAULT_TEMPLATE_STUDENT = "DefaultTemplateStudent";
	public static String DEFAULT_TEMPLATE_EXAMINER = "DefaultTemplateExaminer";
	public static String DEFAULT_TEMPLATE_SP = "DefaultTemplateSP";

	public static String FROM_MAIL_ID = "userId@gmail.com";
	public static String FROM_NAME = "Mail Sender";
	public static String MAIL_SUBJECT = "Invitaion from OSCE";

	// CSV File Upload Path
	public static String CSV_FILEPATH = "osMaEntry/gwt/unibas/role/images/";

	// EXCEL File Upload Path For Learning Objective
	public static String EXCEL_FILEPATH = "osMaEntry/gwt/unibas/role/images/";

	// video path under webapps
	public static String appVideoUploadDirectory = "osMaEntry/gwt/unibas/sp/videos";

	// image path under webapps
	public static String appImageUploadDirectory = "osMaEntry/gwt/unibas/sp/images";

	// Module 9 End
	public static String DEFAULT_IMPORT_EOSCE_PATH = "usr/oscemanager/eOSCE/import/";
	// public static String DEFAULT_IMPORT_EOSCE_PATH = "C:\\oscemanager\\eOSCE\\import\\";

	// Role Module
	public static String ROLE_IMAGE_FILEPATH = "usr/oscemanager/role/images/";
	// public static String ROLE_IMAGE_FILEPATH = "c:\\oscemanager\\role\\images\\";

	// Export OSCE File Path
	public static String EXPORT_OSCE_PROCESSED_FILEPATH = "usr/oscemanager/eosce/export/processed/";
	// public static String EXPORT_OSCE_PROCESSED_FILEPATH = "c:\\oscemanager\\eosce\\export\\processed\\";
	public static String EXPORT_OSCE_UNPROCESSED_FILEPATH = "usr/oscemanager/eosce/export/unprocessed/";
	// public static String EXPORT_OSCE_UNPROCESSED_FILEPATH = "c:\\oscemanager\\eosce\\export\\unprocessed\\";

	public static String DEFAULT_MAIL_TEMPLATE_PATH = "usr/oscemanager/Templates/mailTemplates/";
	// public static String DEFAULT_MAIL_TEMPLATE_PATH = "C:\\oscemanager\\Templates\\mailTemplates\\";

	public static String PRINT_SCHEDULE_TEMPLATE = "usr/oscemanager/Templates/";
	// public static final String PRINT_SCHEDULE_TEMPLATE = "C:\\oscemanager\\Templates\\";

	// Module 8 (Assessment Plan)[

	// path of outside of webapps for parmanent storage of images
	// linux
	public static String localImageUploadDirectory = "usr/oscemanager/sp/images/";
	// windows
	// public static String localImageUploadDirectory = "c:\\oscemanager\\sp\\images\\";

	public static String realImagePath = "";
	// public static String imagesrcPath="/osMaEntry/gwt/unibas/sp/images/";

	// path of outside of webapps for parmanent storage of images
	// linux
	public static String localVideoUploadDirectory = "usr/oscemanager/sp/videos/";
	// windows
	// public static String localVideoUploadDirectory = "c://oscemanager//sp//videos//";

	public static String realVideoPath = "";//
	// public static String videosrcPath="/osMaEntry/gwt/unibas/sp/videos/";

	// Module 8]

}