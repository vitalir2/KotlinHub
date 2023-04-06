import React from 'react';

import {ComponentMeta, ComponentStory} from '@storybook/react';
import {RepositoryMainInfo} from "./RepositoryMainInfo";
import {RepositoryAccessMode} from "../../Repository";

export default {
    title: 'Repository Main Info',
    component: RepositoryMainInfo
} as ComponentMeta<typeof RepositoryMainInfo>;

const Template: ComponentStory<typeof RepositoryMainInfo> = (args) => <RepositoryMainInfo {...args} />;

export const Default = Template.bind({});

Default.args = {
    repository: {
        name: "KotlinHub",
        accessMode: RepositoryAccessMode.PUBLIC,
        cloneUrl: "http://url/KotlinHub",
    },
};
