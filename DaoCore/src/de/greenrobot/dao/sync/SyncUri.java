package de.greenrobot.dao.sync;

import java.util.LinkedList;

/**
 * Created by saulhoward on 7/11/14.
 */
public class SyncUri {

    private final LinkedList<PathPart> path;
    private final Part pagination;
    private final LinkedList<Part> include;

    private volatile String uriString;
    private Class clazz;

    private SyncUri(LinkedList<PathPart> path, Part pagination, LinkedList<Part> include) {
        this.path = path;
        this.pagination = pagination;
        this.include = include;

        for (PathPart part : path) {
            if (part.isClass()) {
                clazz = part.clazz;
            }
        }
    }

    public boolean isList() {
        return path.getLast().isClass();
    }

    public Class getUriClass() {
        return clazz;
    }

    private String makeUriString() {
        StringBuilder builder = new StringBuilder();

        for (Part part : path) {
            builder.append(part.value);
            if (!path.getLast().equals(part)) {
                builder.append("/");
            }
        }

        if (pagination != null || include.size() > 0) {
            builder.append("?");

            if (pagination != null) {
                builder.append(pagination.value);
                if (include.size() > 0) {
                    builder.append("&");
                }
            }

            for (Part part : include) {
                builder.append(part.value);
                if (!include.getLast().equals(part)) {
                    builder.append(",");
                }
            }
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        boolean cached = (uriString != null);
        return cached ? uriString
                : (uriString = makeUriString());
    }

    public static final class Builder {

        private LinkedList<PathPart> path = new LinkedList<PathPart>();
        private Part pagination;
        private LinkedList<Part> include = new LinkedList<Part>();

        private boolean pluralize = false;

        <T extends Part>  Builder part(LinkedList<T> partList, T part) {
            if (part != null) {
                partList.addLast(part);
            }
            return this;
        }

        public Builder pluralizePathNames() {
            pluralize = true;
            return this;
        }

        public Builder appendClass(Class clazz) {
            return part(path, PathPart.fromClass(clazz, pluralize));
        }

        public Builder appendClass(Class clazz, String endpointName) {
            return part(path, PathPart.fromClass(clazz, endpointName));
        }

        public Builder appendId(String id) {
            return part(path, PathPart.fromId(id));
        }

        public Builder appendObject(Class clazz, String id) {
            return part(path, PathPart.fromClass(clazz, pluralize)).part(path, PathPart.fromId(id));
        }

        public Builder appendObject(Class clazz, String endpointName, String id) {
            return part(path, PathPart.fromClass(clazz, endpointName)).part(path, PathPart.fromId(id));
        }

        public Builder appendInclude(String include) {

            if (this.include.size() == 0) {
                include = "include=" + include;
            }

            return part(this.include, Part.from(include));
        }

        public Builder appendPagination(int pageNumber, int itemsPerPage) {
            pagination = Part.from("pageNumber=" + pageNumber + "&itemsPerPage=" + itemsPerPage);
            return this;
        }

        public SyncUri build() {
            return new SyncUri(path, pagination, include);
        }
    }

    static class Part {
        String value;

        Part(String value) {
            this.value = value;
        }

        static Part from(String value) {
            return new Part(value);
        }
    }

    static class PathPart extends Part {

        Class clazz;

        public PathPart(String value, Class clazz) {
            super(value);

            this.clazz = clazz;
        }

        boolean isClass() {
            return clazz != null;
        }

        static PathPart fromClass(Class clazz, boolean pluralize) {
            return from(pluralize(clazz.getSimpleName(), pluralize), clazz);
        }

        static PathPart fromClass(Class clazz, String value) {
            return from(value, clazz);
        }

        static PathPart fromId(String id) {
            return from(id, null);
        }

        static PathPart from(String value, Class clazz) {

            if (value == null) {
                return null;
            }

            return new PathPart(value, clazz);
        }

        static String pluralize(String name, boolean pluralize) {

            if (!pluralize) {
                return name;
            }

            char last = name.charAt(name.length() - 1);

            if (last == 'y') {
                return name.substring(0, name.length() - 1) + "ies";
            }

            return name + "s";
        }
    }
}
