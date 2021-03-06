/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.gradle.language.swift.internal;

import org.gradle.api.file.FileCollection;
import org.gradle.api.internal.file.FileOperations;
import org.gradle.api.internal.provider.LockableProperty;
import org.gradle.api.internal.provider.LockableSetProperty;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;
import org.gradle.internal.Cast;
import org.gradle.language.internal.DefaultBinaryCollection;
import org.gradle.language.nativeplatform.internal.ComponentWithNames;
import org.gradle.language.nativeplatform.internal.DefaultNativeComponent;
import org.gradle.language.nativeplatform.internal.Names;
import org.gradle.language.swift.SwiftBinary;
import org.gradle.language.swift.SwiftComponent;
import org.gradle.language.swift.SwiftVersion;
import org.gradle.nativeplatform.OperatingSystemFamily;
import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform;

import java.util.Collections;

public abstract class DefaultSwiftComponent extends DefaultNativeComponent implements SwiftComponent, ComponentWithNames {
    private final DefaultBinaryCollection<SwiftBinary> binaries;
    private final FileCollection swiftSource;
    private final Property<String> module;
    private final String name;
    private final Names names;
    private final LockableProperty<SwiftVersion> sourceCompatibility;
    private final LockableSetProperty<OperatingSystemFamily> operatingSystems;

    public DefaultSwiftComponent(String name, FileOperations fileOperations, ObjectFactory objectFactory) {
        super(fileOperations);
        this.name = name;
        swiftSource = createSourceView("src/"+ name + "/swift", Collections.singletonList("swift"));
        module = objectFactory.property(String.class);
        sourceCompatibility = new LockableProperty<SwiftVersion>(objectFactory.property(SwiftVersion.class));

        names = Names.of(name);
        binaries = Cast.uncheckedCast(objectFactory.newInstance(DefaultBinaryCollection.class, SwiftBinary.class));
        operatingSystems = new LockableSetProperty<OperatingSystemFamily>(objectFactory.setProperty(OperatingSystemFamily.class));
        operatingSystems.set(Collections.singleton(objectFactory.named(OperatingSystemFamily.class, DefaultNativePlatform.getCurrentOperatingSystem().toFamilyName())));
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Names getNames() {
        return names;
    }

    @Override
    public Property<String> getModule() {
        return module;
    }

    @Override
    public FileCollection getSwiftSource() {
        return swiftSource;
    }

    @Override
    public DefaultBinaryCollection<SwiftBinary> getBinaries() {
        return binaries;
    }

    @Override
    public LockableProperty<SwiftVersion> getSourceCompatibility() {
        return sourceCompatibility;
    }

    @Override
    public LockableSetProperty<OperatingSystemFamily> getOperatingSystems() {
        return operatingSystems;
    }
}
