import React from 'react';

import { ComponentStory, ComponentMeta } from '@storybook/react';
import {SegmentsView} from "./SegmentsView";

export default {
    title: 'Repository Segments',
    component: SegmentsView
} as ComponentMeta<typeof SegmentsView>;

const Template: ComponentStory<typeof SegmentsView> = (args) => <SegmentsView {...args} />;

export const Default = Template.bind({});
export const Root = Template.bind({});

Default.args = {
    segments: [
        "KotlinHub",
        "web",
        "src",
        "app",
    ]
}

Root.args = {
    segments: ["KotlinHub"],
}
