package ca.bc.gov.iamp.bcparis.model;

import ca.bc.gov.iamp.bcparis.exception.message.NotificationTypeNotFound;

public enum NotificationType {

	MQFB_PAN,
	MQFB_NAN,
	MQFB_EXPIRATION,
	MQFB_NONE,
	DEFAULT;

	public static NotificationType GetNotificationType(String feedback) {
		switch (feedback) {
			case "MQFB_PAN":
				return NotificationType.MQFB_PAN;
			case "MQFB_NAN":
				return NotificationType.MQFB_NAN;
			case "MQFB_EXPIRATION":
				return NotificationType.MQFB_EXPIRATION;
			case "MQFB_NONE":
				return NotificationType.MQFB_NONE;
			case "DEFAULT":
				return NotificationType.DEFAULT;
			default:
				throw new NotificationTypeNotFound();
		}
	}

}
