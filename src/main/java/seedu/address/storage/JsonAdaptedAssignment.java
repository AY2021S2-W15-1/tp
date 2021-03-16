package seedu.address.storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.LocalDateTimeUtil;
import seedu.address.model.module.Assignment;
import seedu.address.model.module.Description;

/**
 * Jackson-friendly version of {@link Assignment}.
 */
class JsonAdaptedAssignment {
    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Assignment's %s field is missing!";
    private static final Logger logger = LogsCenter.getLogger(JsonAdaptedAssignment.class);
    public final String description;
    public final String deadline;

    /**
     * Constructs a {@code JsonAdaptedAssignment} with the given {@code description} and {@code deadline}.
     */
    @JsonCreator
    public JsonAdaptedAssignment(@JsonProperty("description") String description,
                                 @JsonProperty("deadline") String deadline) {
        this.description = description;
        this.deadline = deadline;
    }

    /**
     * Converts a given {@code source} into this class for Jackson use.
     */
    public JsonAdaptedAssignment(Assignment source) {
        if (source != null) {
            description = source.description.description;
            deadline = source.deadline.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HHmm"));
        } else {
            description = "Empty";
            deadline = "Empty";
        }
    }


    /**
     * Converts this Jackson-friendly adapted assignment object into the model's {@code assignment} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted assignment.
     */
    public Assignment toModelType() throws IllegalValueException {
        if (description == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Description.class.getSimpleName()));
        }

        final Description modelDescription;
        if (description.equals("")) {
            logger.info("Description for Assignment is empty. Assigning default description");
            modelDescription = Description.defaultDescription();
        } else if (!Description.isValidDescription(description)) {
            throw new IllegalValueException(Description.MESSAGE_CONSTRAINTS);
        } else {
            modelDescription = new Description(description);
        }

        if (deadline == null || !LocalDateTimeUtil.isValidDateTime(deadline)) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    LocalDateTime.class.getSimpleName()));
        }

        final LocalDateTime modelDeadline = LocalDateTime.parse(deadline,
                LocalDateTimeUtil.DATETIME_FORMATTER);

        return new Assignment(modelDescription, modelDeadline);
    }

}