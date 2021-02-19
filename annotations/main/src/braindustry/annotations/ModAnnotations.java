package braindustry.annotations;

import mindustry.annotations.Annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ModAnnotations {
    @Target({ElementType.TYPE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WritableObject{

    }
    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.SOURCE)
    public @interface WritableObjectsConfig {
        Class[] value() default {};
        int offset() default 0;
    }
    public enum Values {
        val;
        Object value;

        public Values setValue(Object value) {
            this.value = value;
            return this;
        }

        public String getToStringValue() {
            return value + "";
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface DefaultValue {
        String value();

        Class[] imports() default {};
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface RulesTable {

    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Rules {

    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Remote {
        /**
         * Specifies the locations from which this method can be invoked.
         */
        Annotations.Loc targets() default Annotations.Loc.server;

        /**
         * Specifies which methods are generated. Only affects server-to-client methods.
         */
        Annotations.Variant variants() default Annotations.Variant.all;

        /**
         * The local locations where this method is called locally, when invoked.
         */
        Annotations.Loc called() default Annotations.Loc.none;

        /**
         * Whether to forward this packet to all other clients upon receival. Client only.
         */
        boolean forward() default false;

        /**
         * Whether the packet for this method is sent with UDP instead of TCP.
         * UDP is faster, but is prone to packet loss and duplication.
         */
        boolean unreliable() default false;

        int replaceLevel() default -1;

        /**
         * Priority of this event.
         */
        Annotations.PacketPriority priority() default Annotations.PacketPriority.normal;

    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface EntityInterface {
    }

    @Retention(RetentionPolicy.SOURCE)
    public @interface Import {
    }
}
