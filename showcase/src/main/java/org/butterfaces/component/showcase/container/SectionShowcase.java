package org.butterfaces.component.showcase.container;

import org.butterfaces.component.showcase.AbstractCodeShowcase;
import org.butterfaces.component.showcase.example.AbstractCodeExample;
import org.butterfaces.component.showcase.example.XhtmlCodeExample;
import org.butterfaces.model.tree.EnumTreeBoxWrapper;

import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Named
@ViewScoped
@SuppressWarnings("serial")
public class SectionShowcase extends AbstractCodeShowcase implements Serializable {

    private AdditionalHeaderType selectedAdditionalHeaderType = AdditionalHeaderType.NONE;
    private String badgeText = null;
    private String label = "label";

    @Override
    public void buildCodeExamples(final List<AbstractCodeExample> codeExamples) {
        final XhtmlCodeExample xhtmlCodeExample = new XhtmlCodeExample(false);

        xhtmlCodeExample.appendInnerContent("        <b:section id=\"input\"");
        xhtmlCodeExample.appendInnerContent("                   label=\"" + this.label + "\"");
        xhtmlCodeExample.appendInnerContent("                   badgeText=\"" + this.badgeText + "\"");
        xhtmlCodeExample.appendInnerContent("                   rendered=\"" + this.isRendered() + "\">");

        if (selectedAdditionalHeaderType == AdditionalHeaderType.TEXT) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"additional-header\">");
            xhtmlCodeExample.appendInnerContent("                additional text");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        } else if (selectedAdditionalHeaderType == AdditionalHeaderType.BUTTON) {
            xhtmlCodeExample.appendInnerContent("            <f:facet name=\"additional-header\">");
            xhtmlCodeExample.appendInnerContent("                <span class=\"btn btn-danger btn-sm\">");
            xhtmlCodeExample.appendInnerContent("                    additional button");
            xhtmlCodeExample.appendInnerContent("                </span>");
            xhtmlCodeExample.appendInnerContent("            </f:facet>");
        }

        xhtmlCodeExample.appendInnerContent("            Lorem ipsum dolor sit amet, consectetuer ...");
        xhtmlCodeExample.appendInnerContent("        </b:section>");
        xhtmlCodeExample.appendInnerContent("        Lorem ipsum dolor sit amet, consectetuer ...", false);

        codeExamples.add(xhtmlCodeExample);
    }

    public List<EnumTreeBoxWrapper> getAdditionalHeaderTypes() {
        final List<EnumTreeBoxWrapper> items = new ArrayList<>();

        for (final AdditionalHeaderType type : AdditionalHeaderType.values()) {
            items.add(new EnumTreeBoxWrapper(type, type.label));
        }
        return items;
    }

    public String getBadgeText() {
        return badgeText;
    }

    public void setBadgeText(String badgeText) {
        this.badgeText = badgeText;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public AdditionalHeaderType getSelectedAdditionalHeaderType() {
        return selectedAdditionalHeaderType;
    }

    public void setSelectedAdditionalHeaderType(AdditionalHeaderType selectedAdditionalHeaderType) {
        this.selectedAdditionalHeaderType = selectedAdditionalHeaderType;
    }
}
