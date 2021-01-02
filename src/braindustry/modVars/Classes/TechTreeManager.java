package braindustry.modVars.Classes;

import arc.func.Cons;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.content.TechTree;
import mindustry.ctype.UnlockableContent;
import mindustry.game.Objectives;
import mindustry.type.ItemStack;

public class TechTreeManager {
    public TechTree.TechNode context = null;
    private TechTreeSub techTreeSub =new TechTreeSub();
    public class TechTreeSub {
        public TechTree.TechNode context = null;
        private void setContext(TechTree.TechNode context){
            this.context=context;
        }
        private void setContext(UnlockableContent content){
            this.context=TechTree.get(content);
        }
        private TechTree.TechNode getContext(){
            return this.context;
        }
        public TechTree.TechNode node(UnlockableContent content, Seq<Objectives.Objective> objectives, Runnable children) {
            return node(content, content.researchRequirements(), objectives, children);
        }

        public TechTree.TechNode node(UnlockableContent content, Runnable children) {
            return node(content, content.researchRequirements(), children);
        }
        public TechTree.TechNode node(UnlockableContent content,  Seq<Objectives.Objective> objectives) {
            return node(content, content.researchRequirements(),objectives, ()->{});
        }

        public TechTree.TechNode node(UnlockableContent content, ItemStack[] requirements, Runnable children) {
            return node(content, requirements, (Seq) null, children);
        }

        public TechTree.TechNode node(UnlockableContent content, ItemStack[] requirements, Seq<Objectives.Objective> objectives, Runnable children) {
            return this.node(content,requirements,objectives, (tree)-> {
                children.run();
            });
        }

        public TechTree.TechNode node(UnlockableContent content, ItemStack[] requirements, Seq<Objectives.Objective> objectives, Cons<TechTreeSub> children) {
            TechTree.TechNode node = new TechTree.TechNode(context, content, requirements);
            if (objectives != null) {
                node.objectives.addAll(objectives);
            }

            TechTree.TechNode prev = context;
            context = node;
            children.get(this);
            context = prev;
            return node;
        }

        public TechTree.TechNode node(UnlockableContent block) {
            return node(block, () -> {
            });
        }

        public TechTree.TechNode nodeProduce(UnlockableContent content, Seq<Objectives.Objective> objectives,  Cons<TechTreeSub> children) {
            return node(content, content.researchRequirements(), objectives.and(new Objectives.Produce(content)), children);
        }

        public TechTree.TechNode nodeProduce(UnlockableContent content,  Runnable children) {
            return nodeProduce(content, new Seq<>(), (tree)->{children.run();});
        }
    }
    public void parentNode(UnlockableContent parent,UnlockableContent content, Seq<Objectives.Objective> objectives,  Cons<TechTreeSub> children){
        parentNode(parent,content, content.researchRequirements(), objectives, children);
    }
    public void parentNode(UnlockableContent parent,UnlockableContent content, Seq<Objectives.Objective> objectives){
        parentNode(parent,content, content.researchRequirements(), objectives, (tree)->{});
    }
    public void parentNode(UnlockableContent parent,UnlockableContent content,  Cons<TechTreeSub> children){
        parentNode(parent,content, content.researchRequirements(), children);
    }
    public void parentNode(UnlockableContent parent,UnlockableContent content ){
        parentNode(parent,content, content.researchRequirements(), (tree)->{});
    }
    public void parentNode(UnlockableContent parent,UnlockableContent content, ItemStack[] requirements,  Cons<TechTreeSub> children){
        parentNode(parent,content, requirements, (Seq) null, children);

    }
    public void parentNode(UnlockableContent parent,UnlockableContent content, ItemStack[] requirements, Seq<Objectives.Objective> objectives,  Cons<TechTreeSub> children){
        techTreeSub.setContext(parent);
        techTreeSub.node(content,requirements,objectives,children);
    }

    @Nullable
    public TechTree.TechNode get(UnlockableContent content) {
        return TechTree.get(content);
    }

    public TechTree.TechNode getNotNull(UnlockableContent content) {
        return TechTree.getNotNull(content);
    }
}
