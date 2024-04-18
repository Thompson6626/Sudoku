package sudoku.problemdomain.Sudoku;
import javax.swing.BorderFactory;
import javax.swing.JFormattedTextField;
import javax.swing.border.Border;
import javax.swing.text.MaskFormatter;
import java.awt.Color;

public class CustomBorderTextField extends JFormattedTextField {

    private final int borderWidth; // Desired border width
    private final Color topBorderColor;
    private final Color bottomBorderColor;
    private final Color leftBorderColor;
    private final Color rightBorderColor;

    public CustomBorderTextField(
            int width,
            Color topColor,
            Color bottomColor,
            Color leftColor,
            Color rightColor,
            AbstractFormatter formatter
    ) {
        super(formatter);
        this.borderWidth = width;
        this.topBorderColor = topColor;
        this.bottomBorderColor = bottomColor;
        this.leftBorderColor = leftColor;
        this.rightBorderColor = rightColor;
        setBorder(createCustomBorder());
    }


    private Border createCustomBorder() {
        // Create individual borders with specified width and color for each side
        Border topBorder = BorderFactory.createLineBorder(topBorderColor, borderWidth);
        Border bottomBorder = BorderFactory.createLineBorder(bottomBorderColor, borderWidth);
        Border leftBorder = BorderFactory.createLineBorder(leftBorderColor, borderWidth);
        Border rightBorder = BorderFactory.createLineBorder(rightBorderColor, borderWidth);

        // Combine borders to create the final border with all sides included

        return BorderFactory.createCompoundBorder(
                topBorder,
                BorderFactory.createCompoundBorder(
                        leftBorder,
                        BorderFactory.createCompoundBorder(
                                bottomBorder,
                                rightBorder
                        )
                )
        );
    }
}