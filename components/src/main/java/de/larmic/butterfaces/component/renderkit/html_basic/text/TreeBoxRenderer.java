package de.larmic.butterfaces.component.renderkit.html_basic.text;

import de.larmic.butterfaces.component.html.text.HtmlTreeBox;
import de.larmic.butterfaces.component.partrenderer.RenderUtils;
import de.larmic.butterfaces.component.renderkit.html_basic.text.part.TrivialComponentsEntriesNodePartRenderer;
import de.larmic.butterfaces.model.tree.Node;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.render.FacesRenderer;
import java.io.IOException;
import java.util.*;

@FacesRenderer(componentFamily = HtmlTreeBox.COMPONENT_FAMILY, rendererType = HtmlTreeBox.RENDERER_TYPE)
public class TreeBoxRenderer extends AbstractTextRenderer<HtmlTreeBox> {

    private final Map<Integer, Node> cachedNodes = new HashMap<>();

    @Override
    protected boolean encodeReadonly() {
        return false;
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component, "butter-component-tags");
    }

    @Override
    protected void encodeEnd(UIComponent component, ResponseWriter writer) throws IOException {
        final HtmlTreeBox treeBox = (HtmlTreeBox) component;

        final Node rootNode = treeBox.getValues();
        // TODO add hideRootNode
        //final List<Node> nodes = treeBox.isHideRootNode() ? rootNode.getSubNodes() : Arrays.asList(rootNode);
        final List<Node> nodes = false ? rootNode.getSubNodes() : Arrays.asList(rootNode);

        this.initCachedNodes(nodes, 0);

        writer.startElement("script", component);
        writer.writeText(RenderUtils.createJQueryPluginCall(component.getClientId(), ".butter-input-component", createJQueryPluginCallTrivial(treeBox, nodes)), null);
        writer.endElement("script");
    }

    @Override
    public Object getConvertedValue(final FacesContext context,
                                    final UIComponent component,
                                    final Object submittedValue) throws ConverterException {
        if (submittedValue == null || "".equals(submittedValue)) {
            return null;
        }

        final String newValue = (String) submittedValue;
        final Integer selectedIndex = Integer.valueOf(newValue);
        return cachedNodes.get(selectedIndex);
    }

    private String createJQueryPluginCallTrivial(final HtmlTreeBox treeBox, final List<Node> nodes) {
        final StringBuilder jQueryPluginCall = new StringBuilder();

        final ArrayList<String> mustacheKeys = new ArrayList<>();

        if (treeBox.getValue() != null) {
            for (Integer index : cachedNodes.keySet()) {
                final Node node = cachedNodes.get(index);
                if (node == treeBox.getValue()) {
                    final String selectedNode = new TrivialComponentsEntriesNodePartRenderer().renderNode(mustacheKeys, cachedNodes, index, node);
                    // TODO create selected entry string
                    break;
                }
            }
        }

        jQueryPluginCall.append("TrivialTreeComboBox({");
        jQueryPluginCall.append("\n    allowFreeText: true,");
        //jQueryPluginCall.append("\n    emptyEntry: {");
        //jQueryPluginCall.append("\n    \"displayValue\": \"Please select...\",");
        //jQueryPluginCall.append("\n    \"imageUrl\": \"-\",");
        //jQueryPluginCall.append("\n    \"additionalInfo\": \"\"");
        //jQueryPluginCall.append("\n    },");
        jQueryPluginCall.append("\n    templates: ['" + TreeRenderer.DEFAULT_TEMPLATE + "'],");
        jQueryPluginCall.append("\n    entries: " + this.renderEntries(nodes, mustacheKeys));
        jQueryPluginCall.append("});");

        return jQueryPluginCall.toString();
    }

    private String renderEntries(final List<Node> nodes, final List<String> mustacheKeys) {
        final StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("[");
        new TrivialComponentsEntriesNodePartRenderer().renderNodes(stringBuilder, nodes, 0, mustacheKeys, cachedNodes);
        stringBuilder.append("]");

        return stringBuilder.toString();
    }

    private int initCachedNodes(final List<Node> nodes,
                                final int index) {
        int newIndex = index;

        for (Node node : nodes) {
            cachedNodes.put(newIndex, node);
            newIndex++;

            if (node.getSubNodes().size() > 0) {
                newIndex = initCachedNodes(node.getSubNodes(), newIndex);
            }
        }

        return newIndex;
    }
}