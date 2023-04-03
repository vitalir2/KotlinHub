import React from 'react';

import { ComponentStory, ComponentMeta } from '@storybook/react';

import {RepositoryContent, RepositoryFileType} from './RepositoryContent';

export default {
    title: 'Repository Content',
    component: RepositoryContent
} as ComponentMeta<typeof RepositoryContent>;

const Template: ComponentStory<typeof RepositoryContent> = (args) => <RepositoryContent {...args} />;

export const Default = Template.bind({});

Default.args = {
    repositoryFiles: [
        {
            name: "Hello",
            type: RepositoryFileType.FOLDER,
        },
        {
            name: "World.txt",
            type: RepositoryFileType.REGULAR,
        },
    ]
}
