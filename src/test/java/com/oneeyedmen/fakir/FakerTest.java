package com.oneeyedmen.fakir;

import org.junit.Test;

import static org.junit.Assert.*;

@SuppressWarnings("UnusedDeclaration")
public class FakerTest {

    public static class ClassToBeFaked {
        private String property;
        private boolean isSomething;

        private ClassToBeFaked() {}

        public String name() {
            return "name";
        }
        public String operation(int arg) {
            return "Value is " + arg;
        }
        public String getProperty() {
            return property;
        }
        public void setProperty(String value) {
            property = value;
        }
        public boolean isSomething() {
            return isSomething;
        }
        public void setSomething(boolean value) {
            isSomething = value;
        }
    }

    @Test public void use_a_field_to_fake_an_accessor() {
        ClassToBeFaked fake = new Faker<ClassToBeFaked>() {
            String name = "bill";
        }.get();

        assertEquals("bill", fake.name());
    }

    @Test public void use_a_method_to_fake_a_method() {
        ClassToBeFaked fake = new Faker<ClassToBeFaked>() {
            String operation(int arg) {
                return String.valueOf(arg);
            }
        }.get();

        assertEquals("42", fake.operation(42));
    }

    @Test public void use_a_field_to_fake_a_property() {
        ClassToBeFaked fake = new Faker<ClassToBeFaked>() {
            String property = "bill";
        }.get();

        assertEquals("bill", fake.getProperty());

        fake.setProperty("fred");
        assertEquals("fred", fake.getProperty());
    }

    @Test public void use_a_field_to_fake_an_is_property() {
        ClassToBeFaked fake = new Faker<ClassToBeFaked>() {
            boolean something = false;
        }.get();

        assertEquals(false, fake.isSomething());

        fake.setSomething(true);
        assertEquals(true, fake.isSomething());
    }

    @Test public void can_delegate_to_an_object() {
        ClassToBeFaked fake = Faker.wrapWith(ClassToBeFaked.class, new Object() {
            String property = "fred";
        });

        assertEquals("fred", fake.getProperty());
        fake.setProperty("bill");
        assertEquals("bill", fake.getProperty());
    }

    @Test public void works_with_abstract_class() {
        abstract class AbstractValueToBeFaked {
            public abstract String getProperty();
            public abstract void setProperty(String value);
        }

        AbstractValueToBeFaked fake = new Faker<AbstractValueToBeFaked>() {
            String property = "bill";
        }.get();

        assertEquals("bill", fake.getProperty());

        fake.setProperty("fred");
        assertEquals("fred", fake.getProperty());
    }

    interface InterfaceToBeFaked {
        public abstract String getProperty();
        public abstract void setProperty(String value);
    }

    @Test public void works_with_interface() {
        InterfaceToBeFaked fake = new Faker<InterfaceToBeFaked>() {
            String property = "bill";
        }.get();

        assertEquals("bill", fake.getProperty());

        fake.setProperty("fred");
        assertEquals("fred", fake.getProperty());
    }

    @Test public void equals_only_considers_identity() {
        ClassToBeFaked fake = Faker.fakeA(ClassToBeFaked.class);
        ClassToBeFaked fake2 = Faker.fakeA(ClassToBeFaked.class);
        assertEquals(fake, fake);
        assertNotEquals(fake, fake2);
    }

    @Test public void returns_an_informative_to_string() {
        ClassToBeFaked fake = Faker.fakeA(ClassToBeFaked.class);
        assertEquals("A fake ClassToBeFaked", fake.toString());
    }

    @Test public void fakes_accessors_by_name() {
        ClassToBeFaked fake = Faker.fakeA(ClassToBeFaked.class);
        assertEquals("name", fake.name());
    }

    @Test public void throws_ClassCastException_on_wrong_type() {
        ClassToBeFaked fake = new Faker<ClassToBeFaked>() {
            int name = 42;
        }.get();

        try {
            fake.name();
            fail();
        } catch (ClassCastException x) {
        }
    }
}
