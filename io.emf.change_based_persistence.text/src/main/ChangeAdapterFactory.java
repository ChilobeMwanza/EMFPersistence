package main;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

public class ChangeAdapterFactory extends AdapterFactoryImpl
{
	protected static final ChangeAdapter ADAPTER =
			new ChangeAdapter();
	
	@Override
	protected Adapter createAdapter(Notifier target)
	{
		return ADAPTER;
	}
	

	@Override
	public boolean isFactoryForType(Object type)
	{
		return type == ChangeAdapter.class;
	}
}